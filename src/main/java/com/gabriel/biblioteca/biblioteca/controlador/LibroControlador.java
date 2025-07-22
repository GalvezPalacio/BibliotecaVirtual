/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gabriel.biblioteca.biblioteca.controlador;

import com.gabriel.biblioteca.biblioteca.modelo.Libro;
import com.gabriel.biblioteca.biblioteca.modelo.LibroCatalogoDTO;
import com.gabriel.biblioteca.biblioteca.repositorio.LibroRepositorio;
import com.gabriel.biblioteca.biblioteca.servicio.LibroServicio;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/libros")
public class LibroControlador {

    private final LibroRepositorio libroRepositorio;

    private final LibroServicio libroServicio;

    public LibroControlador(LibroServicio libroServicio, LibroRepositorio libroRepositorio) {
        this.libroServicio = libroServicio;
        this.libroRepositorio = libroRepositorio;
    }

    // 🟢 GET /api/libros → devuelve todos los libros
    @GetMapping
    public List<Libro> obtenerTodos() {
        return libroServicio.obtenerTodos();
    }
    // 🔍 GET /api/libros/{id} → devuelve un libro específico

    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerPorId(@PathVariable Long id) {
        Libro libro = libroServicio.buscarPorId(id);
        if (libro != null) {
            return ResponseEntity.ok(libro);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // 🟢 POST /api/libros → crear un libro nuevo

    @PostMapping
    public ResponseEntity<Libro> crearLibro(@Valid @RequestBody Libro libro) {
        libro.setDisponible(true);
        Libro guardado = libroServicio.guardarLibro(libro);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizarLibro(@PathVariable Long id, @Valid @RequestBody Libro libroActualizado) {
        Libro actualizado = libroServicio.actualizarLibro(id, libroActualizado);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable Long id) {
        boolean eliminado = libroServicio.eliminarLibro(id);
        if (eliminado) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build();  // 404 Not Found
        }
    }

    @GetMapping("/disponibles")
    @PreAuthorize("hasRole('USUARIO')")
    public List<Libro> obtenerDisponibles() {
        System.out.println("📚 Endpoint /api/libros/disponibles ACCEDIDO");
        return libroServicio.obtenerDisponibles();
    }

    @GetMapping("/catalogo")
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public List<LibroCatalogoDTO> obtenerCatalogoCompleto() {
        return libroServicio.obtenerCatalogo();
    }
}
