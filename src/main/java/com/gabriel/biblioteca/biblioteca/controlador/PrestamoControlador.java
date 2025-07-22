/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gabriel.biblioteca.biblioteca.controlador;

/**
 *
 * @author gabri
 */
import com.gabriel.biblioteca.biblioteca.modelo.Prestamo;
import com.gabriel.biblioteca.biblioteca.servicio.PrestamoServicio;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoControlador {

    private final PrestamoServicio prestamoServicio;

    public PrestamoControlador(PrestamoServicio prestamoServicio) {
        this.prestamoServicio = prestamoServicio;
    }

    @GetMapping
    public List<Prestamo> obtenerTodos() {
        return prestamoServicio.obtenerTodos();
    }

    @PostMapping
    public ResponseEntity<Prestamo> crearPrestamo(@Valid @RequestBody Prestamo prestamo) {
        Prestamo guardado = prestamoServicio.guardarPrestamo(prestamo);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<Prestamo> devolverPrestamo(@PathVariable Long id) {
        Prestamo prestamo = prestamoServicio.buscarPorId(id);

        if (prestamo == null) {
            return ResponseEntity.notFound().build();
        }

        if (prestamo.getFechaDevolucion() != null) {
            return ResponseEntity.badRequest().build(); // Ya estaba devuelto
        }

        prestamo.setFechaDevolucion(LocalDate.now());
        Prestamo actualizado = prestamoServicio.guardarPrestamo(prestamo);

        return ResponseEntity.ok(actualizado);
    }
}
