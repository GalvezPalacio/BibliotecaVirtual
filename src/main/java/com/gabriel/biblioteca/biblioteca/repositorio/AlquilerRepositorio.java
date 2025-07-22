/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.gabriel.biblioteca.biblioteca.repositorio;

/**
 *
 * @author gabri
 */
import com.gabriel.biblioteca.biblioteca.modelo.Alquiler;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlquilerRepositorio extends JpaRepository<Alquiler, Long> {

    List<Alquiler> findByUsuarioId(Long usuarioId);

    List<Alquiler> findByLibroId(Long libroId);

    // 🆕 Añadido para saber quién tiene alquilado un libro ahora mismo
    Optional<Alquiler> findByLibroIdAndFechaDevolucionIsNull(Long libroId);
}
