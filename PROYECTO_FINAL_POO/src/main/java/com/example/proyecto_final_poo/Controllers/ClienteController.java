package com.example.proyecto_final_poo.Controllers;


import com.example.proyecto_final_poo.Models.Cliente;
import com.example.proyecto_final_poo.Models.Vehiculo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    private static final int TAMANO_PAGINA = 8;

    private final JdbcTemplate jdbcTemplate;

    public ClienteController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Cliente mapearCliente(java.sql.ResultSet resultado, int fila) throws java.sql.SQLException {
        Cliente cliente = new Cliente(
                resultado.getInt("id"),
                resultado.getString("nombre"),
                resultado.getString("telefono"),
                resultado.getString("correo")
        );
        cliente.setTotalVehiculos(resultado.getInt("total_vehiculos"));
        return cliente;
    }

    // ============ LISTAR con búsqueda (paginado) ============
    @GetMapping
    public String listar(@RequestParam(required = false) String busqueda,
                         @RequestParam(defaultValue = "1") int pagina,
                         Model model) {

        String condiciones = "";
        List<Object> parametros = new ArrayList<>();

        if (busqueda != null && !busqueda.isBlank()) {
            String comodin = "%" + busqueda.trim() + "%";
            condiciones = " where c.nombre like ? or c.telefono like ? or c.correo like ?";
            parametros.add(comodin);
            parametros.add(comodin);
            parametros.add(comodin);
        }

        // --- Total de registros que cumplen el filtro ---
        int totalRegistros = jdbcTemplate.queryForObject(
                "select count(*) from tbl_cliente c" + condiciones, Integer.class, parametros.toArray());
        int totalPaginas = Math.max(1, (int) Math.ceil(totalRegistros / (double) TAMANO_PAGINA));

        if (pagina < 1) pagina = 1;
        if (pagina > totalPaginas) pagina = totalPaginas;
        int offset = (pagina - 1) * TAMANO_PAGINA;

        // --- Datos de la página actual ---
        String queryDatos = "select c.*, " +
                "(select count(*) from tbl_vehiculo v where v.id_cliente = c.id) as total_vehiculos " +
                "from tbl_cliente c" + condiciones + " order by c.nombre limit ? offset ?";

        List<Object> parametrosDatos = new ArrayList<>(parametros);
        parametrosDatos.add(TAMANO_PAGINA);
        parametrosDatos.add(offset);

        List<Cliente> clientes = jdbcTemplate.query(queryDatos, this::mapearCliente, parametrosDatos.toArray());

        model.addAttribute("clientes", clientes);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("totalPaginas", totalPaginas);
        return "cliente/list";
    }

    // ============ VER DETALLE ============
    @GetMapping("/{id}")
    public String ver(@PathVariable int id, Model model) {
        try {
            Cliente cliente = jdbcTemplate.queryForObject(
                    "select c.*, (select count(*) from tbl_vehiculo v where v.id_cliente = c.id) as total_vehiculos " +
                            "from tbl_cliente c where c.id = ?",
                    this::mapearCliente, id);

            List<Vehiculo> vehiculosCliente = jdbcTemplate.query(
                    "select * from tbl_vehiculo where id_cliente = ? order by fecha_ingreso desc",
                    (r, f) -> new Vehiculo(
                            r.getInt("id"), r.getString("placa"), r.getString("marca"), r.getString("modelo"),
                            r.getInt("anio"), r.getString("color"), r.getInt("id_cliente"),
                            r.getString("estado"), r.getDate("fecha_ingreso").toLocalDate()
                    ), id);

            model.addAttribute("cliente", cliente);
            model.addAttribute("vehiculosCliente", vehiculosCliente);
            return "cliente/view";
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/cliente";
        }
    }

    // ============ FORMULARIO DE CREACIÓN ============
    @GetMapping("/nuevo")
    public String formularioCrear() {
        return "cliente/create";
    }

    // ============ CREAR ============
    @PostMapping("/nuevo")
    public String crear(@RequestParam String nombre,
                        @RequestParam String telefono,
                        @RequestParam String correo,
                        RedirectAttributes redirectAttributes) {

        Integer existentes = jdbcTemplate.queryForObject(
                "select count(*) from tbl_cliente where correo = ?", Integer.class, correo.trim());

        if (existentes != null && existentes > 0) {
            redirectAttributes.addFlashAttribute("error", "Ya existe un cliente registrado con ese correo.");
            return "redirect:/cliente/nuevo";
        }

        jdbcTemplate.update(
                "insert into tbl_cliente(nombre, telefono, correo) values(?,?,?)",
                nombre.trim(), telefono.trim(), correo.trim());

        redirectAttributes.addFlashAttribute("exito", "Cliente registrado correctamente.");
        return "redirect:/cliente";
    }

    // ============ FORMULARIO DE EDICIÓN ============
    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable int id, Model model) {
        try {
            Cliente cliente = jdbcTemplate.queryForObject(
                    "select *, 0 as total_vehiculos from tbl_cliente where id = ?", this::mapearCliente, id);
            model.addAttribute("cliente", cliente);
            return "cliente/edit";
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/cliente";
        }
    }

    // ============ ACTUALIZAR ============
    @PostMapping("/editar/{id}")
    public String actualizar(@PathVariable int id,
                             @RequestParam String nombre,
                             @RequestParam String telefono,
                             @RequestParam String correo,
                             RedirectAttributes redirectAttributes) {

        Integer existentes = jdbcTemplate.queryForObject(
                "select count(*) from tbl_cliente where correo = ? and id <> ?",
                Integer.class, correo.trim(), id);

        if (existentes != null && existentes > 0) {
            redirectAttributes.addFlashAttribute("error", "Ya existe otro cliente registrado con ese correo.");
            return "redirect:/cliente/editar/" + id;
        }

        jdbcTemplate.update(
                "update tbl_cliente set nombre=?, telefono=?, correo=? where id=?",
                nombre.trim(), telefono.trim(), correo.trim(), id);

        redirectAttributes.addFlashAttribute("exito", "Cliente actualizado correctamente.");
        return "redirect:/cliente";
    }

    // ============ ELIMINAR ============
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            jdbcTemplate.update("delete from tbl_cliente where id = ?", id);
            redirectAttributes.addFlashAttribute("exito", "Cliente eliminado correctamente.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "No se puede eliminar: este cliente tiene vehículos o facturas asociadas.");
        }
        return "redirect:/cliente";
    }
}