package com.example.proyecto_final_poo.Controllers;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/factura")
public class FacturaController {

    private static final double PORCENTAJE_IMPUESTO = 0.20;
    private static final int TAMANO_PAGINA = 8;

    private final JdbcTemplate jdbcTemplate;

    public FacturaController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ============ LISTAR (con paginación) ============
    @GetMapping
    public String listar(@RequestParam(required = false) String busqueda,
                         @RequestParam(required = false) String desde,
                         @RequestParam(required = false) String hasta,
                         @RequestParam(defaultValue = "1") int pagina,
                         Model model) {

        String condiciones = " where 1=1";
        List<Object> parametros = new ArrayList<>();

        if (busqueda != null && !busqueda.isBlank()) {
            condiciones += " and (c.nombre like ? or v.placa like ?)";
            String comodin = "%" + busqueda.trim() + "%";
            parametros.add(comodin);
            parametros.add(comodin);
        }
        if (desde != null && !desde.isBlank()) {
            condiciones += " and date(f.fecha) >= ?";
            parametros.add(desde);
        }
        if (hasta != null && !hasta.isBlank()) {
            condiciones += " and date(f.fecha) <= ?";
            parametros.add(hasta);
        }

        // --- Total de registros que cumplen el filtro, para calcular cuántas páginas hay ---
        String queryConteo = "select count(*) from tbl_factura f " +
                "join tbl_cliente c on c.id = f.id_cliente " +
                "join tbl_vehiculo v on v.id = f.id_vehiculo" + condiciones;

        int totalRegistros = jdbcTemplate.queryForObject(queryConteo, Integer.class, parametros.toArray());
        int totalPaginas = Math.max(1, (int) Math.ceil(totalRegistros / (double) TAMANO_PAGINA));

        if (pagina < 1) pagina = 1;
        if (pagina > totalPaginas) pagina = totalPaginas;
        int offset = (pagina - 1) * TAMANO_PAGINA;

        // --- Datos de la página actual ---
        String queryDatos = "select f.id, f.fecha, f.monto_total, f.tipo_reparacion, f.metodo_pago, " +
                "c.nombre as cliente_nombre, v.placa " +
                "from tbl_factura f " +
                "join tbl_cliente c on c.id = f.id_cliente " +
                "join tbl_vehiculo v on v.id = f.id_vehiculo" + condiciones +
                " order by f.fecha desc limit ? offset ?";

        List<Object> parametrosDatos = new ArrayList<>(parametros);
        parametrosDatos.add(TAMANO_PAGINA);
        parametrosDatos.add(offset);

        List<Map<String, Object>> facturas = jdbcTemplate.queryForList(queryDatos, parametrosDatos.toArray());

        model.addAttribute("facturas", facturas);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("totalPaginas", totalPaginas);
        return "factura/list";
    }

    // ============ VER (comprobante) ============
    @GetMapping("/{id}")
    public String ver(@PathVariable int id, Model model) {
        try {
            Map<String, Object> factura = jdbcTemplate.queryForMap(
                    "select f.*, c.nombre as cliente_nombre, c.telefono, c.correo, " +
                            "v.placa, v.marca, v.modelo, v.anio, " +
                            "t.tipo as tarjeta_tipo, t.titular as tarjeta_titular, t.ultimos_digitos as tarjeta_ultimos_digitos " +
                            "from tbl_factura f " +
                            "join tbl_cliente c on c.id = f.id_cliente " +
                            "join tbl_vehiculo v on v.id = f.id_vehiculo " +
                            "left join tbl_tarjeta t on t.id = f.id_tarjeta " +
                            "where f.id = ?", id);

            List<Map<String, Object>> detalle = jdbcTemplate.queryForList(
                    "select d.cantidad, d.precio_unitario, r.nombre as repuesto_nombre " +
                            "from tbl_factura_detalle d join tbl_repuesto r on r.id = d.id_repuesto " +
                            "where d.id_factura = ?", id);

            model.addAttribute("factura", factura);
            model.addAttribute("detalle", detalle);
            return "factura/view";
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/factura";
        }
    }

    // ============ FORMULARIO DE CREACIÓN ============
    @GetMapping("/nueva")
    public String formularioCrear(@RequestParam(required = false) Integer idVehiculo, Model model) {

        List<Map<String, Object>> vehiculos = jdbcTemplate.queryForList(
                "select v.id, v.placa, v.marca, v.modelo, c.id as id_cliente, c.nombre as cliente_nombre " +
                        "from tbl_vehiculo v join tbl_cliente c on c.id = v.id_cliente " +
                        "where v.estado <> 'ENTREGADO' order by v.fecha_ingreso desc");

        List<Map<String, Object>> repuestos = jdbcTemplate.queryForList(
                "select id, nombre, precio, stock from tbl_repuesto where stock > 0 order by nombre");

        // Tarjetas ya registradas por cliente, para reconocerlas sin pedirlas de nuevo
        List<Map<String, Object>> tarjetas = jdbcTemplate.queryForList(
                "select id_cliente, tipo, titular, ultimos_digitos from tbl_tarjeta");

        model.addAttribute("vehiculos", vehiculos);
        model.addAttribute("repuestos", repuestos);
        model.addAttribute("tarjetas", tarjetas);
        model.addAttribute("idVehiculoPreseleccionado", idVehiculo);
        return "factura/create";
    }

    // ============ CREAR (encabezado + detalle + descuento de stock + impuesto + pago) ============
    @PostMapping("/nueva")
    public String crear(@RequestParam int idVehiculo,
                        @RequestParam String tipoReparacion,
                        @RequestParam int manoObra,
                        @RequestParam String metodoPago,
                        @RequestParam(required = false) String usarTarjetaGuardada,
                        @RequestParam(required = false) String numeroTarjeta,
                        @RequestParam(required = false) String titularTarjeta,
                        @RequestParam(required = false) String tipoTarjeta,
                        @RequestParam(required = false) List<Integer> idRepuesto,
                        @RequestParam(required = false) List<Integer> cantidad,
                        RedirectAttributes redirectAttributes) {

        Map<String, Object> vehiculo;
        try {
            vehiculo = jdbcTemplate.queryForMap(
                    "select id_cliente, estado from tbl_vehiculo where id = ?", idVehiculo);
        } catch (EmptyResultDataAccessException e) {
            redirectAttributes.addFlashAttribute("error", "El vehículo seleccionado ya no existe.");
            return "redirect:/factura/nueva";
        }
        int idCliente = (int) vehiculo.get("id_cliente");
        String estadoAnterior = (String) vehiculo.get("estado");

        // ============ Repuestos y subtotal ============
        double subtotalRepuestos = 0;
        List<Double> preciosUnitarios = new ArrayList<>();

        if (idRepuesto != null && cantidad != null && idRepuesto.size() == cantidad.size()) {
            for (int i = 0; i < idRepuesto.size(); i++) {
                if (cantidad.get(i) == null || cantidad.get(i) <= 0) {
                    preciosUnitarios.add(0.0);
                    continue;
                }
                Map<String, Object> repuesto = jdbcTemplate.queryForMap(
                        "select precio, stock from tbl_repuesto where id = ?", idRepuesto.get(i));
                double precio = ((Number) repuesto.get("precio")).doubleValue();
                int stockDisponible = ((Number) repuesto.get("stock")).intValue();

                if (cantidad.get(i) > stockDisponible) {
                    redirectAttributes.addFlashAttribute("error",
                            "No hay suficiente stock para uno de los repuestos seleccionados.");
                    return "redirect:/factura/nueva";
                }

                preciosUnitarios.add(precio);
                subtotalRepuestos += precio * cantidad.get(i);
            }
        }

        // El impuesto del 20% aplica solo sobre el valor de los repuestos, no sobre la mano de obra
        double impuesto = subtotalRepuestos * PORCENTAJE_IMPUESTO;
        double montoTotal = manoObra + subtotalRepuestos + impuesto;

        // ============ Método de pago ============
        Integer idTarjeta = null;

        if ("TARJETA".equals(metodoPago)) {
            if ("true".equals(usarTarjetaGuardada)) {
                // Ya existe una tarjeta registrada para este cliente, la reutilizamos
                idTarjeta = jdbcTemplate.queryForObject(
                        "select id from tbl_tarjeta where id_cliente = ?", Integer.class, idCliente);
            } else {
                if (titularTarjeta == null || titularTarjeta.isBlank()
                        || numeroTarjeta == null || numeroTarjeta.replaceAll("\\D", "").length() < 4) {
                    redirectAttributes.addFlashAttribute("error", "Completá los datos de la tarjeta.");
                    return "redirect:/factura/nueva";
                }

                String soloDigitos = numeroTarjeta.replaceAll("\\D", "");
                String ultimosDigitos = soloDigitos.substring(soloDigitos.length() - 4);

                // Se registra o actualiza la tarjeta del cliente (nunca el número completo, solo los últimos 4 dígitos)
                jdbcTemplate.update(
                        "insert into tbl_tarjeta(id_cliente, tipo, titular, ultimos_digitos, fecha_registro) " +
                                "values(?,?,?,?,?) " +
                                "on duplicate key update tipo=values(tipo), titular=values(titular), " +
                                "ultimos_digitos=values(ultimos_digitos), fecha_registro=values(fecha_registro)",
                        idCliente, tipoTarjeta, titularTarjeta.trim(), ultimosDigitos, LocalDateTime.now());

                idTarjeta = jdbcTemplate.queryForObject(
                        "select id from tbl_tarjeta where id_cliente = ?", Integer.class, idCliente);
            }
        }

        // ============ Encabezado de la factura ============
        jdbcTemplate.update(
                "insert into tbl_factura(id_cliente, id_vehiculo, tipo_reparacion, mano_obra, impuesto, monto_total, metodo_pago, id_tarjeta, fecha) " +
                        "values(?,?,?,?,?,?,?,?,?)",
                idCliente, idVehiculo, tipoReparacion.trim(), manoObra, impuesto, montoTotal, metodoPago, idTarjeta, LocalDateTime.now());

        Integer idFactura = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);

        // ============ Detalle de repuestos + descuento de stock ============
        if (idRepuesto != null) {
            for (int i = 0; i < idRepuesto.size(); i++) {
                if (cantidad.get(i) == null || cantidad.get(i) <= 0) continue;

                jdbcTemplate.update(
                        "insert into tbl_factura_detalle(id_factura, id_repuesto, cantidad, precio_unitario) values(?,?,?,?)",
                        idFactura, idRepuesto.get(i), cantidad.get(i), preciosUnitarios.get(i));

                jdbcTemplate.update(
                        "update tbl_repuesto set stock = stock - ? where id = ?",
                        cantidad.get(i), idRepuesto.get(i));
            }
        }

        // ============ El vehículo pasa a Entregado y queda en el historial ============
        jdbcTemplate.update("update tbl_vehiculo set estado = 'ENTREGADO' where id = ?", idVehiculo);
        jdbcTemplate.update(
                "insert into tbl_historial_estado(id_vehiculo, estado_anterior, estado_nuevo, fecha_cambio) values(?,?,?,?)",
                idVehiculo, estadoAnterior, "ENTREGADO", LocalDateTime.now());

        redirectAttributes.addFlashAttribute("exito", "Factura generada correctamente.");
        return "redirect:/factura/" + idFactura;
    }

    // ============ DIRECTORIO: elegir de qué cliente ver el historial (paginado) ============
    @GetMapping("/historial")
    public String historialDirectorio(@RequestParam(required = false) String busqueda,
                                      @RequestParam(defaultValue = "1") int pagina,
                                      Model model) {

        String condiciones = "";
        List<Object> parametros = new ArrayList<>();
        if (busqueda != null && !busqueda.isBlank()) {
            condiciones = " where c.nombre like ?";
            parametros.add("%" + busqueda.trim() + "%");
        }

        // --- Total de clientes que cumplen el filtro ---
        int totalRegistros = jdbcTemplate.queryForObject(
                "select count(*) from tbl_cliente c" + condiciones, Integer.class, parametros.toArray());
        int totalPaginas = Math.max(1, (int) Math.ceil(totalRegistros / (double) TAMANO_PAGINA));

        if (pagina < 1) pagina = 1;
        if (pagina > totalPaginas) pagina = totalPaginas;
        int offset = (pagina - 1) * TAMANO_PAGINA;

        String queryDatos = "select c.id, c.nombre, c.telefono, " +
                "(select coalesce(sum(monto_total), 0) from tbl_factura f where f.id_cliente = c.id) as total_facturado " +
                "from tbl_cliente c" + condiciones + " order by c.nombre limit ? offset ?";

        List<Object> parametrosDatos = new ArrayList<>(parametros);
        parametrosDatos.add(TAMANO_PAGINA);
        parametrosDatos.add(offset);

        List<Map<String, Object>> clientes = jdbcTemplate.queryForList(queryDatos, parametrosDatos.toArray());

        model.addAttribute("clientes", clientes);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("totalPaginas", totalPaginas);
        return "factura/historial-directorio";
    }

    // ============ HISTORIAL DE FACTURACIÓN POR CLIENTE ============
    @GetMapping("/historialCliente/{idCliente}")
    public String historialPorCliente(@PathVariable int idCliente, Model model) {
        try {
            Map<String, Object> cliente = jdbcTemplate.queryForMap(
                    "select id, nombre, telefono, correo from tbl_cliente where id = ?", idCliente);

            List<Map<String, Object>> facturas = jdbcTemplate.queryForList(
                    "select f.id, f.fecha, f.tipo_reparacion, f.mano_obra, f.monto_total, v.placa, v.marca, v.modelo " +
                            "from tbl_factura f join tbl_vehiculo v on v.id = f.id_vehiculo " +
                            "where f.id_cliente = ? order by f.fecha desc", idCliente);

            Double totalFacturado = jdbcTemplate.queryForObject(
                    "select coalesce(sum(monto_total), 0) from tbl_factura where id_cliente = ?",
                    Double.class, idCliente);

            model.addAttribute("cliente", cliente);
            model.addAttribute("facturas", facturas);
            model.addAttribute("totalFacturado", totalFacturado);
            return "factura/historial";
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/cliente";
        }
    }
}