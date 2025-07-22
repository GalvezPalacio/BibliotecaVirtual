/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gabriel.biblioteca.biblioteca.servicio;

/**
 *
 * @author gabri
 */
import com.gabriel.biblioteca.biblioteca.modelo.Prestamo;
import com.gabriel.biblioteca.biblioteca.repositorio.PrestamoRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrestamoServicio {

    private final PrestamoRepositorio prestamoRepositorio;

    public PrestamoServicio(PrestamoRepositorio prestamoRepositorio) {
        this.prestamoRepositorio = prestamoRepositorio;
    }

    public List<Prestamo> obtenerTodos() {
        return prestamoRepositorio.findAll();
    }

    public Prestamo guardarPrestamo(Prestamo prestamo) {
        return prestamoRepositorio.save(prestamo);
    }

    public Prestamo buscarPorId(Long id) {
        return prestamoRepositorio.findById(id).orElse(null);
    }

    public boolean eliminarPrestamo(Long id) {
        if (prestamoRepositorio.existsById(id)) {
            prestamoRepositorio.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
