/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gabriel.biblioteca.biblioteca.controlador;

/**
 *
 * @author gabri
 */
import com.gabriel.biblioteca.biblioteca.modelo.Genero;
import com.gabriel.biblioteca.biblioteca.repositorio.GeneroRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/generos")
public class GeneroControlador {

    private final GeneroRepositorio generoRepositorio;

    public GeneroControlador(GeneroRepositorio generoRepositorio) {
        this.generoRepositorio = generoRepositorio;
    }

    @PostMapping
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public ResponseEntity<Genero> crearGenero(@RequestBody Genero genero) {
        System.out.println("📦 POST /api/generos recibido: " + genero.getNombre());
        Genero nuevo = generoRepositorio.save(genero);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }
}
