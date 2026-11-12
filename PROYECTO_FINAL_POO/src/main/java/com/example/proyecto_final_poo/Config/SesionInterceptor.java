package com.example.proyecto_final_poo.Config;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class SesionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Object idUsuario = request.getSession().getAttribute("idUsuario");

        if (idUsuario == null) {
            response.sendRedirect(request.getContextPath() + "/usuario/login");
            return false; // corta la petición, nunca llega al controller
        }

        return true; 
    }
}