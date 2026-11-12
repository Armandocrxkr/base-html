package com.example.proyecto_final_poo.Controllers;


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
import java.util.Map;

@Controller
@RequestMapping("/proveedor")
public class ProveedorController {

    private static final int TAMANO_PAGINA = 8;

    private final JdbcTemplate jdbcTemplate;

    public ProveedorController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Proveedor es una pantalla exclusiva de Administrador
    private boolean noEsAdministrador(HttpSession session) {
        return !"ADMINISTRADOR".equals(session.getAttribute("rolUsuario"));
    }

    // ============ LISTAR con búsqueda (paginado) ============
    @GetMapping
    public String listar(@RequestParam(required = false) String busqueda,
                         @RequestParam(defaultValue = "1") int pagina,
                         HttpSession session, Model model) {
        if (noEsAdministrador(session)) {
            return "redirect:/inicio";
        }

        String condiciones = "";
        List<Object> parametros = new ArrayList<>();
        if (busqueda != null && !busqueda.isBlank()) {
            String comodin = "%" + busqueda.trim() + "%";
            condiciones = " where nombre like ? or contacto like ? or telefono like ?";
            parametros.add(comodin);
            parametros.add(comodin);
            parametros.add(comodin);
        }

        // --- Total de registros que cumplen el filtro ---
        int totalRegistros = jdbcTemplate.queryForObject(
                "select count(*) from tbl_proveedor" + condiciones, Integer.class, parametros.toArray());
        int totalPaginas = Math.max(1, (int) Math.ceil(totalRegistros / (double) TAMANO_PAGINA));

        if (pagina < 1) pagina = 1;
        if (pagina > totalPaginas) pagina = totalPaginas;
        int offset = (pagina - 1) * TAMANO_PAGINA;

        // --- Datos de la página actual ---
        String queryDatos = "select * from tbl_proveedor" + condiciones + " order by nombre limit ? offset ?";
        List<Object> parametrosDatos = new ArrayList<>(parametros);
        parametrosDatos.add(TAMANO_PAGINA);
        parametrosDatos.add(offset);

        List<Map<String, Object>> proveedores = jdbcTemplate.queryForList(queryDatos, parametrosDatos.toArray());

        model.addAttribute("proveedores", proveedores);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("totalPaginas", totalPaginas);
        return "proveedor/list";
    }

    // ============ VER DETALLE ============
    @GetMapping("/{id}")
    public String ver(@PathVariable int id, HttpSession session, Model model) {
        if (noEsAdministrador(session)) {
            return "redirect:/inicio";
        }

        try {
            Map<String, Object> proveedor = jdbcTemplate.queryForMap(
                    "select * from tbl_proveedor where id = ?", id);

            List<Map<String, Object>> repuestos = jdbcTemplate.queryForList(
                    "select id, nombre, categoria, stock from tbl_repuesto where id_proveedor = ? order by nombre", id);

            model.addAttribute("proveedor", proveedor);
            model.addAttribute("repuestos", repuestos);
            return "proveedor/view";
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/proveedor";
        }
    }

    // ============ FORMULARIO DE CREACIÓN ============
    @GetMapping("/nuevo")
    public String formularioCrear(HttpSession session) {
        if (noEsAdministrador(session)) {
            return "redirect:/inicio";
        }
        return "proveedor/create";
    }

    // ============ CREAR ============
    @PostMapping("/nuevo")
    public String crear(@RequestParam String nombre,
                        @RequestParam(required = false) String contacto,
                        @RequestParam String telefono,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {

        if (noEsAdministrador(session)) {
            return "redirect:/inicio";
        }

        Integer existentes = jdbcTemplate.queryForObject(
                "select count(*) from tbl_proveedor where nombre = ?", Integer.class, nombre.trim());

        if (existentes != null && existentes > 0) {
            redirectAttributes.addFlashAttribute("error", "Ya existe un proveedor registrado con ese nombre.");
            return "redirect:/proveedor/nuevo";
        }

        jdbcTemplate.update(
                "insert into tbl_proveedor(nombre, contacto, telefono) values(?,?,?)",
                nombre.trim(), contacto != null ? contacto.trim() : null, telefono.trim());

        redirectAttributes.addFlashAttribute("exito", "Proveedor registrado correctamente.");
        return "redirect:/proveedor";
    }

    // ============ FORMULARIO DE EDICIÓN ============
    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable int id, HttpSession session, Model model) {
        if (noEsAdministrador(session)) {
            return "redirect:/inicio";
        }

        try {
            Map<String, Object> proveedor = jdbcTemplate.queryForMap(
                    "select * from tbl_proveedor where id = ?", id);
            model.addAttribute("proveedor", proveedor);
            return "proveedor/edit";
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/proveedor";
        }
    }

    // ============ ACTUALIZAR ============
    @PostMapping("/editar/{id}")
    public String actualizar(@PathVariable int id,
                             @RequestParam String nombre,
                             @RequestParam(required = false) String contacto,
                             @RequestParam String telefono,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        if (noEsAdministrador(session)) {
            return "redirect:/inicio";
        }

        Integer existentes = jdbcTemplate.queryForObject(
                "select count(*) from tbl_proveedor where nombre = ? and id <> ?",
                Integer.class, nombre.trim(), id);

        if (existentes != null && existentes > 0) {
            redirectAttributes.addFlashAttribute("error", "Ya existe otro proveedor registrado con ese nombre.");
            return "redirect:/proveedor/editar/" + id;
        }

        jdbcTemplate.update(
                "update tbl_proveedor set nombre=?, contacto=?, telefono=? where id=?",
                nombre.trim(), contacto != null ? contacto.trim() : null, telefono.trim(), id);

        redirectAttributes.addFlashAttribute("exito", "Proveedor actualizado correctamente.");
        return "redirect:/proveedor";
    }

    // ============ ELIMINAR ============
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (noEsAdministrador(session)) {
            return "redirect:/inicio";
        }

        try {
            jdbcTemplate.update("delete from tbl_proveedor where id = ?", id);
            redirectAttributes.addFlashAttribute("exito", "Proveedor eliminado correctamente.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "No se puede eliminar: este proveedor tiene repuestos o lotes asociados.");
        }
        return "redirect:/proveedor";
    }
}