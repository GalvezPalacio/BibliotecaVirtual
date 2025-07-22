/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gabriel.biblioteca.biblioteca.controlador;

/**
 *
 * @author gabri
 */
import com.gabriel.biblioteca.biblioteca.configuracion.ProveedorJWT;
import com.gabriel.biblioteca.biblioteca.modelo.Usuario;
import com.gabriel.biblioteca.biblioteca.servicio.ServicioUsuarioDetalles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.AuthenticationException;

@RestController
@RequestMapping("/auth")
public class ControladorSeguridad {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ServicioUsuarioDetalles servicioUsuarioDetalles;

    @Autowired
    private ProveedorJWT proveedorJWT;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String contrasena = loginData.get("contrasena");

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, contrasena)
            );

            UserDetails userDetails = servicioUsuarioDetalles.loadUserByUsername(email);
            String token = proveedorJWT.generarToken(userDetails);

            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("token", token);
            return respuesta;

        } catch (AuthenticationException e) {
            throw new RuntimeException("Credenciales inválidas");
        }
    }
}
