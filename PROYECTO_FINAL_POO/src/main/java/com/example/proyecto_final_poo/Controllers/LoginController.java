package com.example.proyecto_final_poo.Controllers;

import com.example.proyecto_final_poo.Models.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    private final JdbcTemplate jdbcTemplate;

    public LoginController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/usuario/login")
    public String login() {

        return "usuario/login";
    }

    @PostMapping("/usuario/login")
    public String autenticar(@RequestParam String usuario,
                             @RequestParam String password,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        String query = "select * from tbl_usuario where usuario = ? and password = ?";

        try {
            Usuario usuarioEncontrado = jdbcTemplate.queryForObject(
                    query,
                    (resultado, fila) -> new Usuario(
                            resultado.getInt("id"),
                            resultado.getString("nombre"),
                            resultado.getString("usuario"),
                            resultado.getString("password"),
                            resultado.getString("rol")
                    ),
                    usuario, password
            );

            // Creación de la sesión: guardamos solo lo necesario, nunca la contraseña
            session.setAttribute("idUsuario", usuarioEncontrado.getId());
            session.setAttribute("nombreUsuario", usuarioEncontrado.getNombre());
            session.setAttribute("rolUsuario", usuarioEncontrado.getRol());

            return "redirect:/inicio";

        } catch (EmptyResultDataAccessException e) {
            // No hubo ninguna fila que coincida con usuario + password
            redirectAttributes.addFlashAttribute("error", "Usuario o contraseña incorrectos.");
            return "redirect:/usuario/login";
        }
    }

    @GetMapping("/usuario/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/usuario/login";
    }
}
