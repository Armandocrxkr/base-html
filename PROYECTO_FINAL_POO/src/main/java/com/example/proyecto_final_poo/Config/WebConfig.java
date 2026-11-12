package com.example.proyecto_final_poo.Config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SesionInterceptor())
                .addPathPatterns("/**") // por defecto, protege todo
                .excludePathPatterns(
                        "/",                    // landing page
                        "/usuario/login",       // pantalla de login (GET y POST)
                        "/usuario/registro",    // pantalla de registro (GET y POST)
                        "/img/**",              // imágenes estáticas 
                        "/css/**",              // estilos estáticos
                        "/js/**",               // scripts estáticos
                        "/error"                // página de error de Spring
                );
    }
}