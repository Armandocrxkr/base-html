package com.example.proyecto_final_poo.Controllers;


import jakarta.servlet.http.HttpSession;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/lote")
public class LoteController {

    private static final int TAMANO_PAGINA = 8;

    private final JdbcTemplate jdbcTemplate;

    public LoteController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Centraliza el chequeo de rol: Lote es una pantalla exclusiva de Administrador
    private boolean noEsAdministrador(HttpSession session) {
        return !"ADMINISTRADOR".equals(session.getAttribute("rolUsuario"));
    }

    // ============ LISTAR (paginado) ============
    @GetMapping
    public String listar(@RequestParam(defaultValue = "1") int pagina, HttpSession session, Model model) {
        if (noEsAdministrador(session)) {
            return "redirect:/inicio";
        }

        // --- Total de registros ---
        int totalRegistros = jdbcTemplate.queryForObject("select count(*) from tbl_lote", Integer.class);
        int totalPaginas = Math.max(1, (int) Math.ceil(totalRegistros / (double) TAMANO_PAGINA));

        if (pagina < 1) pagina = 1;
        if (pagina > totalPaginas) pagina = totalPaginas;
        int offset = (pagina - 1) * TAMANO_PAGINA;

        List<Map<String, Object>> lotes = jdbcTemplate.queryForList(
                "select l.id, l.fecha, l.estado, p.nombre as proveedor_nombre, " +
                        "(select count(*) from tbl_lote_detalle d where d.id_lote = l.id) as total_items " +
                        "from tbl_lote l join tbl_proveedor p on p.id = l.id_proveedor " +
                        "order by l.fecha desc limit ? offset ?",
                TAMANO_PAGINA, offset);

        model.addAttribute("lotes", lotes);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("totalPaginas", totalPaginas);
        return "lote/list";
    }

    // ============ VER DETALLE ============
    @GetMapping("/{id}")
    public String ver(@PathVariable int id, HttpSession session, Model model) {
        if (noEsAdministrador(session)) {
            return "redirect:/inicio";
        }

        try {
            Map<String, Object> lote = jdbcTemplate.queryForMap(
                    "select l.id, l.fecha, l.estado, p.nombre as proveedor_nombre " +
                            "from tbl_lote l join tbl_proveedor p on p.id = l.id_proveedor where l.id = ?", id);

            List<Map<String, Object>> detalle = jdbcTemplate.queryForList(
                    "select d.cantidad, r.nombre as repuesto_nombre, r.categoria " +
                            "from tbl_lote_detalle d join tbl_repuesto r on r.id = d.id_repuesto " +
                            "where d.id_lote = ?", id);

            model.addAttribute("lote", lote);
            model.addAttribute("detalle", detalle);
            return "lote/view";
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/lote";
        }
    }

    // ============ FORMULARIO DE CREACIÓN ============
    @GetMapping("/nuevo")
    public String formularioCrear(HttpSession session, Model model) {
        if (noEsAdministrador(session)) {
            return "redirect:/inicio";
        }

        model.addAttribute("proveedores", jdbcTemplate.query(
                "select id, nombre from tbl_proveedor order by nombre",
                (r, f) -> new Object[]{r.getInt("id"), r.getString("nombre")}));
        model.addAttribute("repuestos", jdbcTemplate.queryForList(
                "select id, nombre, categoria from tbl_repuesto order by nombre"));
        return "lote/create";
    }

    // ============ CREAR (encabezado + detalle en una sola operación) ============
    @PostMapping("/nuevo")
    public String crear(@RequestParam int idProveedor,
                        @RequestParam List<Integer> idRepuesto,
                        @RequestParam List<Integer> cantidad,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {

        if (noEsAdministrador(session)) {
            return "redirect:/inicio";
        }

        if (idRepuesto.isEmpty() || idRepuesto.size() != cantidad.size()) {
            redirectAttributes.addFlashAttribute("error", "Agregá al menos un repuesto con su cantidad.");
            return "redirect:/lote/nuevo";
        }

        // Encabezado del lote
        jdbcTemplate.update(
                "insert into tbl_lote(id_proveedor, fecha, estado) values(?,?,?)",
                idProveedor, LocalDate.now(), "PENDIENTE");

        Integer idLote = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);

        // Detalle: una fila por cada repuesto agregado al pedido
        for (int i = 0; i < idRepuesto.size(); i++) {
            if (cantidad.get(i) != null && cantidad.get(i) > 0) {
                jdbcTemplate.update(
                        "insert into tbl_lote_detalle(id_lote, id_repuesto, cantidad) values(?,?,?)",
                        idLote, idRepuesto.get(i), cantidad.get(i));
            }
        }

        redirectAttributes.addFlashAttribute("exito", "Pedido de lote registrado correctamente.");
        return "redirect:/lote";
    }

    // ============ RECIBIR (marca el lote como recibido y suma el stock) ============
    @PostMapping("/{id}/recibir")
    public String recibir(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (noEsAdministrador(session)) {
            return "redirect:/inicio";
        }

        String estadoActual;
        try {
            estadoActual = jdbcTemplate.queryForObject(
                    "select estado from tbl_lote where id = ?", String.class, id);
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/lote";
        }

        if ("RECIBIDO".equals(estadoActual)) {
            redirectAttributes.addFlashAttribute("error", "Este lote ya fue recibido anteriormente.");
            return "redirect:/lote/" + id;
        }

        List<Map<String, Object>> detalle = jdbcTemplate.queryForList(
                "select id_repuesto, cantidad from tbl_lote_detalle where id_lote = ?", id);

        for (Map<String, Object> linea : detalle) {
            int idRepuesto = (int) linea.get("id_repuesto");
            int cantidad = (int) linea.get("cantidad");
            jdbcTemplate.update(
                    "update tbl_repuesto set stock = stock + ? where id = ?", cantidad, idRepuesto);
        }

        jdbcTemplate.update("update tbl_lote set estado = ? where id = ?", "RECIBIDO", id);

        redirectAttributes.addFlashAttribute("exito", "Lote recibido: el stock de los repuestos ya se actualizó.");
        return "redirect:/lote/" + id;
    }

    // ============ ELIMINAR (solo si sigue pendiente) ============
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (noEsAdministrador(session)) {
            return "redirect:/inicio";
        }

        String estadoActual;
        try {
            estadoActual = jdbcTemplate.queryForObject(
                    "select estado from tbl_lote where id = ?", String.class, id);
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/lote";
        }

        if ("RECIBIDO".equals(estadoActual)) {
            redirectAttributes.addFlashAttribute("error",
                    "No se puede eliminar: este lote ya fue recibido y su stock ya se aplicó al inventario.");
            return "redirect:/lote";
        }

        jdbcTemplate.update("delete from tbl_lote_detalle where id_lote = ?", id);
        jdbcTemplate.update("delete from tbl_lote where id = ?", id);

        redirectAttributes.addFlashAttribute("exito", "Pedido de lote eliminado correctamente.");
        return "redirect:/lote";
    }
}