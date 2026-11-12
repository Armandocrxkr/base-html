package com.example.proyecto_final_poo.Controllers;


import com.example.proyecto_final_poo.Models.Repuesto;
import jakarta.servlet.http.HttpSession;
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
@RequestMapping("/repuesto")
public class RepuestoController {

    private static final int STOCK_BAJO_LIMITE = 5;
    private static final int TAMANO_PAGINA = 8;

    private final JdbcTemplate jdbcTemplate;

    public RepuestoController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Inventario completo (crear, editar, eliminar) es exclusivo de Administrador;
    // el Vendedor solo puede consultar el catálogo.
    private boolean noEsAdministrador(HttpSession session) {
        return !"ADMINISTRADOR".equals(session.getAttribute("rolUsuario"));
    }

    private Repuesto mapearRepuesto(java.sql.ResultSet r, int fila) throws java.sql.SQLException {
        return new Repuesto(
                r.getInt("id"),
                r.getString("nombre"),
                r.getString("categoria"),
                r.getDouble("precio"),
                r.getInt("stock"),
                r.getObject("id_proveedor") != null ? r.getInt("id_proveedor") : 0
        );
    }

    // ============ VER DETALLE ============
    @GetMapping("/{id}")
    public String ver(@PathVariable int id, Model model) {
        try {
            Repuesto repuesto = jdbcTemplate.queryForObject(
                    "select * from tbl_repuesto where id = ?", this::mapearRepuesto, id);

            String nombreProveedor = null;
            if (repuesto.getIdProveedor() != 0) {
                nombreProveedor = jdbcTemplate.queryForObject(
                        "select nombre from tbl_proveedor where id = ?", String.class, repuesto.getIdProveedor());
            }

            model.addAttribute("repuesto", repuesto);
            model.addAttribute("nombreProveedor", nombreProveedor);
            model.addAttribute("stockBajoLimite", STOCK_BAJO_LIMITE);
            return "repuesto/view";
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/repuesto/catalogo";
        }
    }

    // ============ LISTAR (inventario, solo administrador, paginado) ============
    @GetMapping
    public String listar(@RequestParam(required = false) String nombre,
                         @RequestParam(required = false) String categoria,
                         @RequestParam(defaultValue = "1") int pagina,
                         HttpSession session,
                         Model model) {

        if (noEsAdministrador(session)) {
            return "redirect:/repuesto/catalogo";
        }

        String condiciones = " where 1=1";
        List<Object> parametros = new ArrayList<>();

        if (nombre != null && !nombre.isBlank()) {
            condiciones += " and nombre like ?";
            parametros.add("%" + nombre.trim() + "%");
        }
        if (categoria != null && !categoria.isBlank()) {
            condiciones += " and categoria = ?";
            parametros.add(categoria);
        }

        // --- Total de registros que cumplen el filtro ---
        String queryConteo = "select count(*) from tbl_repuesto" + condiciones;
        int totalRegistros = jdbcTemplate.queryForObject(queryConteo, Integer.class, parametros.toArray());
        int totalPaginas = Math.max(1, (int) Math.ceil(totalRegistros / (double) TAMANO_PAGINA));

        if (pagina < 1) pagina = 1;
        if (pagina > totalPaginas) pagina = totalPaginas;
        int offset = (pagina - 1) * TAMANO_PAGINA;

        // --- Datos de la página actual ---
        String queryDatos = "select * from tbl_repuesto" + condiciones + " order by nombre limit ? offset ?";
        List<Object> parametrosDatos = new ArrayList<>(parametros);
        parametrosDatos.add(TAMANO_PAGINA);
        parametrosDatos.add(offset);

        List<Repuesto> repuestos = jdbcTemplate.query(queryDatos, this::mapearRepuesto, parametrosDatos.toArray());
        List<String> categorias = jdbcTemplate.queryForList(
                "select distinct categoria from tbl_repuesto order by categoria", String.class);

        model.addAttribute("repuestos", repuestos);
        model.addAttribute("categorias", categorias);
        model.addAttribute("stockBajoLimite", STOCK_BAJO_LIMITE);
        model.addAttribute("totalRegistros", totalRegistros);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("totalPaginas", totalPaginas);
        return "repuesto/list";
    }

    // ============ CATÁLOGO (consulta para vendedor y administrador, paginado) ============
    @GetMapping("/catalogo")
    public String catalogo(@RequestParam(required = false) String nombre,
                           @RequestParam(required = false) String categoria,
                           @RequestParam(defaultValue = "1") int pagina,
                           Model model) {

        String condiciones = " where stock > 0";
        List<Object> parametros = new ArrayList<>();

        if (nombre != null && !nombre.isBlank()) {
            condiciones += " and nombre like ?";
            parametros.add("%" + nombre.trim() + "%");
        }
        if (categoria != null && !categoria.isBlank()) {
            condiciones += " and categoria = ?";
            parametros.add(categoria);
        }

        // --- Total de registros que cumplen el filtro ---
        String queryConteo = "select count(*) from tbl_repuesto" + condiciones;
        int totalRegistros = jdbcTemplate.queryForObject(queryConteo, Integer.class, parametros.toArray());
        int totalPaginas = Math.max(1, (int) Math.ceil(totalRegistros / (double) TAMANO_PAGINA));

        if (pagina < 1) pagina = 1;
        if (pagina > totalPaginas) pagina = totalPaginas;
        int offset = (pagina - 1) * TAMANO_PAGINA;

        // --- Datos de la página actual ---
        String queryDatos = "select * from tbl_repuesto" + condiciones + " order by nombre limit ? offset ?";
        List<Object> parametrosDatos = new ArrayList<>(parametros);
        parametrosDatos.add(TAMANO_PAGINA);
        parametrosDatos.add(offset);

        List<Repuesto> repuestos = jdbcTemplate.query(queryDatos, this::mapearRepuesto, parametrosDatos.toArray());
        List<String> categorias = jdbcTemplate.queryForList(
                "select distinct categoria from tbl_repuesto order by categoria", String.class);

        model.addAttribute("repuestos", repuestos);
        model.addAttribute("categorias", categorias);
        model.addAttribute("totalRegistros", totalRegistros);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("totalPaginas", totalPaginas);
        return "repuesto/catalogo";
    }

    // ============ FORMULARIO DE CREACIÓN ============
    @GetMapping("/nuevo")
    public String formularioCrear(HttpSession session, Model model) {
        if (noEsAdministrador(session)) {
            return "redirect:/repuesto/catalogo";
        }
        model.addAttribute("proveedores", jdbcTemplate.query(
                "select id, nombre from tbl_proveedor order by nombre",
                (r, f) -> new Object[]{r.getInt("id"), r.getString("nombre")}));
        model.addAttribute("categorias", jdbcTemplate.queryForList(
                "select distinct categoria from tbl_repuesto order by categoria", String.class));
        return "repuesto/create";
    }

    // ============ CREAR ============
    @PostMapping("/nuevo")
    public String crear(@RequestParam String nombre,
                        @RequestParam String categoria,
                        @RequestParam int precio,
                        @RequestParam(required = false) String idProveedor,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {

        if (noEsAdministrador(session)) {
            return "redirect:/repuesto/catalogo";
        }

        Integer existentes = jdbcTemplate.queryForObject(
                "select count(*) from tbl_repuesto where nombre = ? and categoria = ?",
                Integer.class, nombre.trim(), categoria.trim());

        if (existentes != null && existentes > 0) {
            redirectAttributes.addFlashAttribute("error", "Ya existe un repuesto con ese nombre en esa categoría.");
            return "redirect:/repuesto/nuevo";
        }

        Integer idProveedorFinal = (idProveedor != null && !idProveedor.isBlank())
                ? Integer.valueOf(idProveedor) : null;

        // El stock inicial siempre entra en 0: se llena únicamente al recibir un pedido de lote
        jdbcTemplate.update(
                "insert into tbl_repuesto(nombre, categoria, precio, stock, id_proveedor) values(?,?,?,0,?)",
                nombre.trim(), categoria.trim(), precio, idProveedorFinal);

        redirectAttributes.addFlashAttribute("exito", "Repuesto registrado correctamente.");
        return "redirect:/repuesto";
    }

    // ============ FORMULARIO DE EDICIÓN ============
    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable int id, HttpSession session, Model model) {
        if (noEsAdministrador(session)) {
            return "redirect:/repuesto/catalogo";
        }
        try {
            Repuesto repuesto = jdbcTemplate.queryForObject(
                    "select * from tbl_repuesto where id = ?", this::mapearRepuesto, id);
            model.addAttribute("repuesto", repuesto);
            model.addAttribute("proveedores", jdbcTemplate.query(
                    "select id, nombre from tbl_proveedor order by nombre",
                    (r, f) -> new Object[]{r.getInt("id"), r.getString("nombre")}));
            model.addAttribute("categorias", jdbcTemplate.queryForList(
                    "select distinct categoria from tbl_repuesto order by categoria", String.class));
            return "repuesto/edit";
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/repuesto";
        }
    }

    // ============ ACTUALIZAR ============
    @PostMapping("/editar/{id}")
    public String actualizar(@PathVariable int id,
                             @RequestParam String nombre,
                             @RequestParam String categoria,
                             @RequestParam int precio,
                             @RequestParam(required = false) String idProveedor,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        if (noEsAdministrador(session)) {
            return "redirect:/repuesto/catalogo";
        }

        Integer existentes = jdbcTemplate.queryForObject(
                "select count(*) from tbl_repuesto where nombre = ? and categoria = ? and id <> ?",
                Integer.class, nombre.trim(), categoria.trim(), id);

        if (existentes != null && existentes > 0) {
            redirectAttributes.addFlashAttribute("error", "Ya existe otro repuesto con ese nombre en esa categoría.");
            return "redirect:/repuesto/editar/" + id;
        }

        Integer idProveedorFinal = (idProveedor != null && !idProveedor.isBlank())
                ? Integer.valueOf(idProveedor) : null;

        // El stock no se toca aquí: solo cambia al recibir un pedido de lote
        jdbcTemplate.update(
                "update tbl_repuesto set nombre=?, categoria=?, precio=?, id_proveedor=? where id=?",
                nombre.trim(), categoria.trim(), precio, idProveedorFinal, id);

        redirectAttributes.addFlashAttribute("exito", "Repuesto actualizado correctamente.");
        return "redirect:/repuesto";
    }

    // ============ ELIMINAR ============
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (noEsAdministrador(session)) {
            return "redirect:/repuesto/catalogo";
        }
        try {
            jdbcTemplate.update("delete from tbl_repuesto where id = ?", id);
            redirectAttributes.addFlashAttribute("exito", "Repuesto eliminado correctamente.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "No se puede eliminar: este repuesto está asociado a lotes o facturas.");
        }
        return "redirect:/repuesto";
    }
}