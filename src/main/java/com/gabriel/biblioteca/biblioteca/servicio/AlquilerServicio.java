/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gabriel.biblioteca.biblioteca.servicio;

/**
 *
 * @author gabri
 */
import com.gabriel.biblioteca.biblioteca.modelo.Alquiler;
import com.gabriel.biblioteca.biblioteca.modelo.Libro;
import com.gabriel.biblioteca.biblioteca.modelo.Usuario;
import com.gabriel.biblioteca.biblioteca.repositorio.AlquilerRepositorio;
import com.gabriel.biblioteca.biblioteca.repositorio.LibroRepositorio;
import com.gabriel.biblioteca.biblioteca.repositorio.UsuarioRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AlquilerServicio {

    private final AlquilerRepositorio alquilerRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final LibroRepositorio libroRepositorio;

    public AlquilerServicio(AlquilerRepositorio alquilerRepositorio,
            UsuarioRepositorio usuarioRepositorio,
            LibroRepositorio libroRepositorio) {
        this.alquilerRepositorio = alquilerRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.libroRepositorio = libroRepositorio;
    }

    public List<Alquiler> obtenerTodos() {
        return alquilerRepositorio.findAll();
    }

    public Optional<Alquiler> obtenerPorId(Long id) {
        return alquilerRepositorio.findById(id);
    }

    public List<Alquiler> obtenerPorUsuario(Long usuarioId) {
        return alquilerRepositorio.findByUsuarioId(usuarioId);
    }

    public Alquiler guardar(Alquiler alquiler) {
        return alquilerRepositorio.save(alquiler);
    }

    public void eliminar(Long id) {
        alquilerRepositorio.deleteById(id);
    }

    @Transactional
    public Alquiler marcarComoDevuelto(Long alquilerId) {
        Alquiler alquiler = alquilerRepositorio.findById(alquilerId)
                .orElseThrow(() -> new RuntimeException("Alquiler no encontrado"));

        if (alquiler.getFechaDevolucion() != null) {
            throw new RuntimeException("El libro ya fue devuelto");
        }

        alquiler.setFechaDevolucion(LocalDate.now());

        // También marcamos el libro como disponible
        Libro libro = alquiler.getLibro();
        libro.setDisponible(true);

        return alquilerRepositorio.save(alquiler);
    }

    public void devolverLibro(Long idAlquiler) {
        Alquiler alquiler = alquilerRepositorio.findById(idAlquiler)
                .orElseThrow(() -> new RuntimeException("Alquiler no encontrado"));

        if (alquiler.getFechaDevolucion() != null) {
            throw new RuntimeException("Este libro ya fue devuelto");
        }

        alquiler.setFechaDevolucion(LocalDate.now());

        // Marcar libro como disponible
        Libro libro = alquiler.getLibro();
        libro.setDisponible(true);

        alquilerRepositorio.save(alquiler);
        libroRepositorio.save(libro);
    }

    // 🆕 Nuevo método: alquilar libro
    public void alquilarLibro(String email, Long idLibro) {
        Usuario usuario = usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Libro libro = libroRepositorio.findById(idLibro)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        if (!libro.isDisponible()) {
            throw new RuntimeException("Este libro no está disponible");
        }

        Alquiler alquiler = new Alquiler();
        alquiler.setUsuario(usuario);
        alquiler.setLibro(libro);
        alquiler.setFechaAlquiler(LocalDate.now());
        alquiler.setFechaDevolucion(null);

        alquilerRepositorio.save(alquiler);

        libro.setDisponible(false);
        libroRepositorio.save(libro);
    }
}
