package com.example.proyecto_final_poo.Controllers;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistroController {

    private final JdbcTemplate jdbcTemplate;

    public RegistroController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/usuario/registro")
    public String registro() {
        return "usuario/registro";
    }

    @PostMapping("/usuario/registro")
    public String registrar(@RequestParam String nombre,
                            @RequestParam String usuario,
                            @RequestParam String password,
                            @RequestParam String confirmar,
                            RedirectAttributes redirectAttributes) {

        if (!password.equals(confirmar)) {
            redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden.");
            return "redirect:/usuario/registro";
        }

        String queryValidar = "select count(*) from tbl_usuario where usuario = ?";
        int existentes = jdbcTemplate.queryForObject(queryValidar, Integer.class, usuario);

        if (existentes > 0) {
            redirectAttributes.addFlashAttribute("error", "Ese usuario ya existe, elegí otro.");
            return "redirect:/usuario/registro";
        }

        String queryInsertar = "insert into tbl_usuario(nombre, usuario, password, rol) values(?,?,?,?)";
        int resp = jdbcTemplate.update(queryInsertar, nombre, usuario, password, "VENDEDOR");

        if (resp == 0) {
            redirectAttributes.addFlashAttribute("error", "No se pudo crear la cuenta, intentá de nuevo.");
            return "redirect:/usuario/registro";
        }

        redirectAttributes.addFlashAttribute("exito", "Cuenta creada correctamente. Ya podés iniciar sesión.");
        return "redirect:/usuario/login";
    }
}