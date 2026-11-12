package com.example.proyecto_final_poo.Controllers;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/usuario")
public class PerfilController {

    private final JdbcTemplate jdbcTemplate;
    private final HttpSession session;

    public PerfilController(JdbcTemplate jdbcTemplate, HttpSession session) {
        this.jdbcTemplate = jdbcTemplate;
        this.session = session;
    }

    @GetMapping("/perfil")
    public String perfil(Model model) {

        Object idUsuarioSesion = session.getAttribute("idUsuario");
        if (idUsuarioSesion == null) {
            return "redirect:/usuario/login";
        }
        int idUsuario = ((Number) idUsuarioSesion).intValue();

        Map<String, Object> usuario;
        try {
            usuario = jdbcTemplate.queryForMap(
                    "select id, nombre, usuario, rol from tbl_usuario where id = ?", idUsuario);
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/usuario/login";
        }

        // Todas las ventas realizadas por este usuario (vendedor o administrador)
        List<Map<String, Object>> ventas = jdbcTemplate.queryForList(
                "select f.id, f.fecha, f.monto_total, f.tipo_reparacion, " +
                        "c.nombre as cliente_nombre, v.placa " +
                        "from tbl_factura f " +
                        "join tbl_cliente c on c.id = f.id_cliente " +
                        "join tbl_vehiculo v on v.id = f.id_vehiculo " +
                        "where f.id_usuario = ? " +
                        "order by f.fecha desc", idUsuario);

        int totalVentas = ventas.size();

        Double totalGenerado = jdbcTemplate.queryForObject(
                "select coalesce(sum(monto_total), 0) from tbl_factura where id_usuario = ?",
                Double.class, idUsuario);

        model.addAttribute("usuario", usuario);
        model.addAttribute("ventas", ventas);
        model.addAttribute("totalVentas", totalVentas);
        model.addAttribute("totalGenerado", totalGenerado);
        return "usuario/perfil";
    }
}