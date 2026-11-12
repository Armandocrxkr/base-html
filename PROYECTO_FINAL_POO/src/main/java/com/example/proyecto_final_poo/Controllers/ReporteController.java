package com.example.proyecto_final_poo.Controllers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reporte")
public class ReporteController {

    private final JdbcTemplate jdbcTemplate;
    private final HttpSession session;

    public ReporteController(JdbcTemplate jdbcTemplate, HttpSession session) {
        this.jdbcTemplate = jdbcTemplate;
        this.session = session;
    }

    // ============ REPORTE DE VENDEDORES ============
    @GetMapping
    public String reporteVendedores(@RequestParam(required = false) String desde,
                                    @RequestParam(required = false) String hasta,
                                    @RequestParam(required = false) Integer idUsuario,
                                    Model model) {

        // --- Solo administradores pueden ver reportes ---
        Object rol = session.getAttribute("rolUsuario");
        if (rol == null || !rol.equals("ADMINISTRADOR")) {
            return "redirect:/inicio";
        }

        // ============ RESUMEN POR VENDEDOR ============
        StringBuilder queryResumen = new StringBuilder(
                "select u.id, u.nombre, " +
                        "count(fv.id) as total_ventas, " +
                        "coalesce(sum(fv.total_repuestos),0) as total_repuestos, " +
                        "coalesce(sum(fv.monto_total),0) as total_generado, " +
                        "round(coalesce(avg(fv.monto_total),0),2) as promedio_venta, " +
                        "max(fv.fecha) as ultima_venta " +
                        "from tbl_usuario u " +
                        "left join ( " +
                        "   select f.id, f.id_usuario, f.fecha, f.monto_total, " +
                        "          coalesce((select sum(d.cantidad) from tbl_factura_detalle d where d.id_factura = f.id),0) as total_repuestos " +
                        "   from tbl_factura f where 1=1");

        List<Object> parametrosResumen = new ArrayList<>();
        if (desde != null && !desde.isBlank()) {
            queryResumen.append(" and date(f.fecha) >= ?");
            parametrosResumen.add(desde);
        }
        if (hasta != null && !hasta.isBlank()) {
            queryResumen.append(" and date(f.fecha) <= ?");
            parametrosResumen.add(hasta);
        }
        queryResumen.append(" ) fv on fv.id_usuario = u.id " +
                "where u.rol = 'VENDEDOR' " +
                "group by u.id, u.nombre " +
                "order by total_generado desc");

        List<Map<String, Object>> resumenVendedores = jdbcTemplate.queryForList(
                queryResumen.toString(), parametrosResumen.toArray());

        // ============ DETALLE DE FACTURAS DEL VENDEDOR SELECCIONADO ============
        List<Map<String, Object>> facturasVendedor = null;
        String nombreVendedorSeleccionado = null;

        if (idUsuario != null) {
            StringBuilder queryDetalle = new StringBuilder(
                    "select f.id, f.fecha, f.monto_total, f.tipo_reparacion, " +
                            "c.nombre as cliente_nombre, v.placa " +
                            "from tbl_factura f " +
                            "join tbl_cliente c on c.id = f.id_cliente " +
                            "join tbl_vehiculo v on v.id = f.id_vehiculo " +
                            "where f.id_usuario = ?");
            List<Object> parametrosDetalle = new ArrayList<>();
            parametrosDetalle.add(idUsuario);

            if (desde != null && !desde.isBlank()) {
                queryDetalle.append(" and date(f.fecha) >= ?");
                parametrosDetalle.add(desde);
            }
            if (hasta != null && !hasta.isBlank()) {
                queryDetalle.append(" and date(f.fecha) <= ?");
                parametrosDetalle.add(hasta);
            }
            queryDetalle.append(" order by f.fecha desc");

            facturasVendedor = jdbcTemplate.queryForList(queryDetalle.toString(), parametrosDetalle.toArray());

            for (Map<String, Object> v : resumenVendedores) {
                if (((Number) v.get("id")).intValue() == idUsuario) {
                    nombreVendedorSeleccionado = (String) v.get("nombre");
                    break;
                }
            }
        }

        model.addAttribute("resumenVendedores", resumenVendedores);
        model.addAttribute("facturasVendedor", facturasVendedor);
        model.addAttribute("idUsuarioSeleccionado", idUsuario);
        model.addAttribute("nombreVendedorSeleccionado", nombreVendedorSeleccionado);
        return "reporte/list";
    }
}