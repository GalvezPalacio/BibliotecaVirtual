/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gabriel.biblioteca.biblioteca.configuracion;

/**
 *
 * @author gabri
 */
import com.gabriel.biblioteca.biblioteca.servicio.ServicioUsuarioDetalles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import org.springframework.security.config.Customizer;

@EnableMethodSecurity
@Configuration
public class SeguridadConfiguracion {

    private final ServicioUsuarioDetalles servicioUsuarioDetalles;
    private final FiltroJWT filtroJWT;

    public SeguridadConfiguracion(ServicioUsuarioDetalles servicioUsuarioDetalles, FiltroJWT filtroJWT) {
        this.servicioUsuarioDetalles = servicioUsuarioDetalles;
        this.filtroJWT = filtroJWT;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults()) // ✅ así se usa en Spring Boot moderno
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/api/alquileres/todos").hasRole("BIBLIOTECARIO")
                .requestMatchers("/api/usuarios/usuarios/todos").hasRole("BIBLIOTECARIO")
                .requestMatchers("/api/alquileres/mios").hasRole("USUARIO")
                .requestMatchers("/api/alquileres/**").hasRole("USUARIO")
                .requestMatchers("/api/libros/disponibles").hasRole("USUARIO")
                .requestMatchers("/api/libros/catalogo").hasRole("BIBLIOTECARIO")
                .requestMatchers("/api/bibliotecario/**").hasRole("BIBLIOTECARIO")
                .requestMatchers("/api/autores").hasRole("BIBLIOTECARIO")
                .requestMatchers("/api/generos").hasRole("BIBLIOTECARIO")
                .requestMatchers(
                        "/gestionar_usuarios.js", "/gestionar_usuarios.html",
                        "/catalogo.html", "/catalogo.js",
                        "/login.html", "/alquilar.js",
                        "/alquileres.js", "/alquileres.html", "/alquilar.html",
                        "/inicio.html", "/estilos.css", "/login.js", "/inicio.js",
                        "/registro.html", "/registro.js"
                ).permitAll()
                .requestMatchers("/img/**", "/favicon.ico").permitAll()
                .requestMatchers("/api/usuarios/rol", "/api/usuarios/yo").permitAll()
                .requestMatchers("/api/usuarios").permitAll()
                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(filtroJWT, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(servicioUsuarioDetalles);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ Configuración CORS global para permitir llamadas desde el frontend
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080")); // O usa List.of("*") para permitir todos
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
