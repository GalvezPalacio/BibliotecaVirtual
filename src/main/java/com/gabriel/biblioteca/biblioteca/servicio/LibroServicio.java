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
import com.gabriel.biblioteca.biblioteca.modelo.LibroCatalogoDTO;
import com.gabriel.biblioteca.biblioteca.repositorio.AlquilerRepositorio;
import com.gabriel.biblioteca.biblioteca.repositorio.LibroRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibroServicio {

    private final LibroRepositorio libroRepositorio;
    private final AlquilerRepositorio alquilerRepositorio;

    public LibroServicio(LibroRepositorio libroRepositorio, AlquilerRepositorio alquilerRepositorio) {
        this.libroRepositorio = libroRepositorio;
        this.alquilerRepositorio = alquilerRepositorio;
    }

    public List<Libro> obtenerTodos() {
        return libroRepositorio.findAll();
    }

    public Libro guardarLibro(Libro libro) {
        return libroRepositorio.save(libro);
    }

    public Libro actualizarLibro(Long id, Libro libroActualizado) {
        return libroRepositorio.findById(id).map(libro -> {
            libro.setTitulo(libroActualizado.getTitulo());
            libro.setAutorId(libroActualizado.getAutorId());
            libro.setGeneroId(libroActualizado.getGeneroId());
            libro.setAnio(libroActualizado.getAnio());
            libro.setDisponible(libroActualizado.isDisponible());
            return libroRepositorio.save(libro);
        }).orElse(null);
    }

    public boolean eliminarLibro(Long id) {
        if (libroRepositorio.existsById(id)) {
            libroRepositorio.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Libro buscarPorId(Long id) {
        return libroRepositorio.findById(id).orElse(null);
    }

    public List<Libro> obtenerDisponibles() {
        return libroRepositorio.findByDisponibleTrue();
    }

    // 🆕 Método para mostrar el catálogo completo con nombre del usuario si está alquilado
    public List<LibroCatalogoDTO> obtenerCatalogo() {
        List<Libro> libros = libroRepositorio.findAll();

        return libros.stream().map(libro -> {
            String nombreUsuario = null;

            if (!libro.isDisponible()) {
                Optional<Alquiler> alquilerActivo = alquilerRepositorio.findByLibroIdAndFechaDevolucionIsNull(libro.getId());
                if (alquilerActivo.isPresent()) {
                    nombreUsuario = alquilerActivo.get().getUsuario().getNombre();
                }
            }

            return new LibroCatalogoDTO(
                    libro.getId(),
                    libro.getTitulo(),
                    libro.getAnio(),
                    libro.isDisponible(),
                    nombreUsuario
            );
        }).collect(Collectors.toList());
    }
}
