package com.example.proyecto_final_poo.Controllers;

import com.example.proyecto_final_poo.Models.Vehiculo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private static final int STOCK_BAJO_LIMITE = 5;

    private final JdbcTemplate jdbcTemplate;

    public HomeController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/inicio")
    public String inicio(Model model) {

        Integer vehiculosRecibidos = jdbcTemplate.queryForObject(
                "select count(*) from tbl_vehiculo where estado = ?",
                Integer.class, "RECIBIDO"
        );

        Integer vehiculosEnReparacion = jdbcTemplate.queryForObject(
                "select count(*) from tbl_vehiculo where estado in ('EN_DIAGNOSTICO','EN_REPARACION')",
                Integer.class
        );

        Integer vehiculosListos = jdbcTemplate.queryForObject(
                "select count(*) from tbl_vehiculo where estado = ?",
                Integer.class, "LISTO_PARA_ENTREGA"
        );

        Integer repuestosStockBajo = jdbcTemplate.queryForObject(
                "select count(*) from tbl_repuesto where stock < ?",
                Integer.class, STOCK_BAJO_LIMITE
        );

        String queryActivos = "select * from tbl_vehiculo where estado <> 'ENTREGADO' order by fecha_ingreso desc limit 6";
        List<Vehiculo> vehiculosActivos = jdbcTemplate.query(
                queryActivos,
                (resultado, fila) -> new Vehiculo(
                        resultado.getInt("id"),
                        resultado.getString("placa"),
                        resultado.getString("marca"),
                        resultado.getString("modelo"),
                        resultado.getInt("anio"),
                        resultado.getString("color"),
                        resultado.getInt("id_cliente"),
                        resultado.getString("estado"),
                        resultado.getDate("fecha_ingreso").toLocalDate()
                )
        );

        // ============ Últimas facturas (actividad reciente) ============
        List<Map<String, Object>> ultimasFacturas = jdbcTemplate.queryForList(
                "select f.id, f.fecha, f.monto_total, c.nombre as cliente_nombre, v.placa " +
                        "from tbl_factura f " +
                        "join tbl_cliente c on c.id = f.id_cliente " +
                        "join tbl_vehiculo v on v.id = f.id_vehiculo " +
                        "order by f.fecha desc limit 5"
        );

        // ============ Repuestos en estado crítico (detalle, no solo el conteo) ============
        List<Map<String, Object>> repuestosCriticos = jdbcTemplate.queryForList(
                "select id, nombre, stock from tbl_repuesto where stock < ? order by stock asc limit 5",
                STOCK_BAJO_LIMITE
        );

        model.addAttribute("vehiculosRecibidos", vehiculosRecibidos);
        model.addAttribute("vehiculosEnReparacion", vehiculosEnReparacion);
        model.addAttribute("vehiculosListos", vehiculosListos);
        model.addAttribute("repuestosStockBajo", repuestosStockBajo);
        model.addAttribute("vehiculosActivos", vehiculosActivos);
        model.addAttribute("ultimasFacturas", ultimasFacturas);
        model.addAttribute("repuestosCriticos", repuestosCriticos);

        return "inicio";
    }
}