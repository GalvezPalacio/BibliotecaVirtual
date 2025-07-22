/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gabriel.biblioteca.biblioteca.controlador;

/**
 *
 * @author gabri
 */
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bibliotecario")
public class ControladorBibliotecario {

    @GetMapping("/privado")
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public String seccionPrivada() {
        return "¡Bienvenido, bibliotecario!";
    }
}
