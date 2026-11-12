package com.example.proyecto_final_poo.Controllers;

import com.example.proyecto_final_poo.Models.Vehiculo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/vehiculo")
public class VehiculoController {

    private static final int TAMANO_PAGINA = 8;

    private final JdbcTemplate jdbcTemplate;

    public VehiculoController(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper reutilizado en todos los métodos de este controller
    private Vehiculo mapearVehiculo(java.sql.ResultSet resultado, int fila) throws java.sql.SQLException {
        return new Vehiculo(
                resultado.getInt("id"),
                resultado.getString("placa"),
                resultado.getString("marca"),
                resultado.getString("modelo"),
                resultado.getInt("anio"),
                resultado.getString("color"),
                resultado.getInt("id_cliente"),
                resultado.getString("estado"),
                resultado.getDate("fecha_ingreso").toLocalDate()
        );
    }

    // ============ LISTAR con filtros (paginado) ============
    @GetMapping
    public String listar(@RequestParam(required = false) String placa,
                         @RequestParam(required = false) String marca,
                         @RequestParam(required = false) String modelo,
                         @RequestParam(defaultValue = "1") int pagina,
                         Model model) {

        String condiciones = " where 1=1";
        List<Object> parametros = new ArrayList<>();

        if (placa != null && !placa.isBlank()) {
            condiciones += " and placa like ?";
            parametros.add("%" + placa.trim() + "%");
        }
        if (marca != null && !marca.isBlank()) {
            condiciones += " and marca = ?";
            parametros.add(marca);
        }
        if (modelo != null && !modelo.isBlank()) {
            condiciones += " and modelo = ?";
            parametros.add(modelo);
        }

        // --- Total de registros que cumplen el filtro ---
        String queryConteo = "select count(*) from tbl_vehiculo" + condiciones;
        int totalRegistros = jdbcTemplate.queryForObject(queryConteo, Integer.class, parametros.toArray());
        int totalPaginas = Math.max(1, (int) Math.ceil(totalRegistros / (double) TAMANO_PAGINA));

        if (pagina < 1) pagina = 1;
        if (pagina > totalPaginas) pagina = totalPaginas;
        int offset = (pagina - 1) * TAMANO_PAGINA;

        // --- Datos de la página actual ---
        String queryDatos = "select * from tbl_vehiculo" + condiciones + " order by fecha_ingreso desc limit ? offset ?";
        List<Object> parametrosDatos = new ArrayList<>(parametros);
        parametrosDatos.add(TAMANO_PAGINA);
        parametrosDatos.add(offset);

        List<Vehiculo> vehiculos = jdbcTemplate.query(queryDatos, this::mapearVehiculo, parametrosDatos.toArray());

        List<String> marcas = jdbcTemplate.queryForList(
                "select distinct marca from tbl_vehiculo order by marca", String.class);
        List<String> modelos = jdbcTemplate.queryForList(
                "select distinct modelo from tbl_vehiculo order by modelo", String.class);
        List<java.util.Map<String, Object>> marcaModelo = jdbcTemplate.queryForList(
                "select distinct marca, modelo from tbl_vehiculo order by marca, modelo");

        model.addAttribute("vehiculos", vehiculos);
        model.addAttribute("marcas", marcas);
        model.addAttribute("modelos", modelos);
        model.addAttribute("marcaModelo", marcaModelo);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("totalPaginas", totalPaginas);

        return "vehiculo/list";
    }

    // ============ VER DETALLE ============
    @GetMapping("/{id}")
    public String ver(@PathVariable int id, Model model) {
        try {
            Vehiculo vehiculo = jdbcTemplate.queryForObject(
                    "select * from tbl_vehiculo where id = ?", this::mapearVehiculo, id);

            String nombreCliente = jdbcTemplate.queryForObject(
                    "select nombre from tbl_cliente where id = ?", String.class, vehiculo.getIdCliente());

            List<java.util.Map<String, Object>> historial = jdbcTemplate.queryForList(
                    "select estado_anterior, estado_nuevo, fecha_cambio from tbl_historial_estado " +
                            "where id_vehiculo = ? order by fecha_cambio desc", id);

            model.addAttribute("vehiculo", vehiculo);
            model.addAttribute("nombreCliente", nombreCliente);
            model.addAttribute("historial", historial);
            return "vehiculo/view";
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/vehiculo";
        }
    }

    // ============ FORMULARIO DE CREACIÓN ============
    @GetMapping("/nuevo")
    public String formularioCrear(Model model) {
        model.addAttribute("clientes", jdbcTemplate.query(
                "select id, nombre from tbl_cliente order by nombre",
                (r, f) -> new Object[]{r.getInt("id"), r.getString("nombre")}));
        model.addAttribute("marcas", jdbcTemplate.queryForList(
                "select distinct marca from tbl_vehiculo order by marca", String.class));
        model.addAttribute("modelos", jdbcTemplate.queryForList(
                "select distinct modelo from tbl_vehiculo order by modelo", String.class));
        model.addAttribute("marcaModelo", jdbcTemplate.queryForList(
                "select distinct marca, modelo from tbl_vehiculo order by marca, modelo"));
        return "vehiculo/create";
    }

    // ============ CREAR ============
    @PostMapping("/nuevo")
    public String crear(@RequestParam String placa,
                        @RequestParam String marca,
                        @RequestParam String modelo,
                        @RequestParam int anio,
                        @RequestParam String color,
                        @RequestParam int idCliente,
                        RedirectAttributes redirectAttributes) {

        String placaFormateada = placa.trim().toUpperCase();

        if (!placaFormateada.matches("^[A-Z]{3}-\\d{4}$")) {
            redirectAttributes.addFlashAttribute("error", "La placa debe tener el formato ABC-1234 (tres letras, guion, cuatro números).");
            return "redirect:/vehiculo/nuevo";
        }

        int anioActual = java.time.Year.now().getValue();
        if (anio < 1980 || anio > anioActual + 1) {
            redirectAttributes.addFlashAttribute("error", "El año del vehículo no es válido.");
            return "redirect:/vehiculo/nuevo";
        }

        Integer existentes = jdbcTemplate.queryForObject(
                "select count(*) from tbl_vehiculo where placa = ?", Integer.class, placaFormateada);

        if (existentes != null && existentes > 0) {
            redirectAttributes.addFlashAttribute("error", "Ya existe un vehículo registrado con esa placa.");
            return "redirect:/vehiculo/nuevo";
        }

        String query = "insert into tbl_vehiculo(placa, marca, modelo, anio, color, id_cliente, estado, fecha_ingreso) " +
                "values(?,?,?,?,?,?,?,?)";

        jdbcTemplate.update(query, placaFormateada, marca, modelo, anio, color,
                idCliente, "RECIBIDO", LocalDate.now());

        redirectAttributes.addFlashAttribute("exito", "Vehículo registrado correctamente.");
        return "redirect:/vehiculo";
    }

    // ============ FORMULARIO DE EDICIÓN ============
    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable int id, Model model) {
        try {
            Vehiculo vehiculo = jdbcTemplate.queryForObject(
                    "select * from tbl_vehiculo where id = ?", this::mapearVehiculo, id);
            model.addAttribute("vehiculo", vehiculo);
            model.addAttribute("clientes", jdbcTemplate.query(
                    "select id, nombre from tbl_cliente order by nombre",
                    (r, f) -> new Object[]{r.getInt("id"), r.getString("nombre")}));
            model.addAttribute("marcas", jdbcTemplate.queryForList(
                    "select distinct marca from tbl_vehiculo order by marca", String.class));
            model.addAttribute("marcaModelo", jdbcTemplate.queryForList(
                    "select distinct marca, modelo from tbl_vehiculo order by marca, modelo"));
            return "vehiculo/edit";
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/vehiculo";
        }
    }

    // ============ ACTUALIZAR ============
    @PostMapping("/editar/{id}")
    public String actualizar(@PathVariable int id,
                             @RequestParam String placa,
                             @RequestParam String marca,
                             @RequestParam String modelo,
                             @RequestParam int anio,
                             @RequestParam String color,
                             @RequestParam int idCliente,
                             RedirectAttributes redirectAttributes) {

        Integer existentes = jdbcTemplate.queryForObject(
                "select count(*) from tbl_vehiculo where placa = ? and id <> ?",
                Integer.class, placa.trim(), id);

        if (existentes != null && existentes > 0) {
            redirectAttributes.addFlashAttribute("error", "Ya existe otro vehículo registrado con esa placa.");
            return "redirect:/vehiculo/editar/" + id;
        }

        String query = "update tbl_vehiculo set placa=?, marca=?, modelo=?, anio=?, color=?, id_cliente=? where id=?";
        jdbcTemplate.update(query, placa.trim().toUpperCase(), marca, modelo, anio, color, idCliente, id);

        redirectAttributes.addFlashAttribute("exito", "Vehículo actualizado correctamente.");
        return "redirect:/vehiculo";
    }

    // ============ ELIMINAR ============
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            jdbcTemplate.update("delete from tbl_vehiculo where id = ?", id);
            redirectAttributes.addFlashAttribute("exito", "Vehículo eliminado correctamente.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "No se puede eliminar: este vehículo tiene historial o facturas asociadas.");
        }
        return "redirect:/vehiculo";
    }

    // ============ CAMBIAR ESTADO (y registrar historial) ============
    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable int id,
                                @RequestParam String nuevoEstado,
                                RedirectAttributes redirectAttributes) {

        String estadoActual;
        try {
            estadoActual = jdbcTemplate.queryForObject(
                    "select estado from tbl_vehiculo where id = ?", String.class, id);
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/vehiculo";
        }

        if (estadoActual.equals(nuevoEstado)) {
            redirectAttributes.addFlashAttribute("error", "El vehículo ya se encuentra en ese estado.");
            return "redirect:/vehiculo/" + id;
        }

        jdbcTemplate.update("update tbl_vehiculo set estado = ? where id = ?", nuevoEstado, id);

        jdbcTemplate.update(
                "insert into tbl_historial_estado(id_vehiculo, estado_anterior, estado_nuevo, fecha_cambio) " +
                        "values(?,?,?,?)",
                id, estadoActual, nuevoEstado, java.time.LocalDateTime.now());

        redirectAttributes.addFlashAttribute("exito", "Estado actualizado correctamente.");
        return "redirect:/vehiculo/" + id;
    }
}
