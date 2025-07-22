/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gabriel.biblioteca.biblioteca.configuracion;

/**
 *
 * @author gabri
 */
import com.gabriel.biblioteca.biblioteca.modelo.UsuarioAutenticado;
import com.gabriel.biblioteca.biblioteca.servicio.ServicioUsuarioDetalles;
import com.gabriel.biblioteca.biblioteca.servicio.UsuarioDetalles;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FiltroJWT extends OncePerRequestFilter {

    private final ProveedorJWT proveedorJWT;
    private final ServicioUsuarioDetalles servicioUsuarioDetalles;

    public FiltroJWT(ProveedorJWT proveedorJWT, ServicioUsuarioDetalles servicioUsuarioDetalles) {
        this.proveedorJWT = proveedorJWT;
        this.servicioUsuarioDetalles = servicioUsuarioDetalles;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String emailUsuario = proveedorJWT.extraerUsuario(token);

            if (emailUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = servicioUsuarioDetalles.loadUserByUsername(emailUsuario);

                if (proveedorJWT.esTokenValido(token, userDetails)) {

                    // Obtener tipo desde UsuarioDetalles
                    String tipo = "usuario"; // valor por defecto

                    if (userDetails instanceof UsuarioDetalles detalles) {
                        tipo = detalles.getUsuario().getTipo(); // accedemos al tipo real desde tu entidad

                        // 👉 Añadido: mostrar las autoridades que se están usando
                        System.out.println("🔐 Rol del usuario autenticado: " + detalles.getAuthorities());
                    }

                    UsernamePasswordAuthenticationToken authToken
                            = new UsernamePasswordAuthenticationToken(
                                    userDetails, // usamos el objeto que implementa UserDetails
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
