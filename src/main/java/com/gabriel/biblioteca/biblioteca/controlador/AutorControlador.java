/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gabriel.biblioteca.biblioteca.controlador;

/**
 *
 * @author gabri
 */
import com.gabriel.biblioteca.biblioteca.modelo.Autor;
import com.gabriel.biblioteca.biblioteca.repositorio.AutorRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/autores")
public class AutorControlador {

    @Autowired
    private AutorRepositorio autorRepositorio;

    @PostMapping
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public ResponseEntity<Autor> crearAutor(@RequestBody Autor autor) {
        System.out.println("➡️ POST /api/autores recibido: " + autor.getNombre());
        Autor nuevo = autorRepositorio.save(autor);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }
}
