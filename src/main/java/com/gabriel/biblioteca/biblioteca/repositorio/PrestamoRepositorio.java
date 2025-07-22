/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.gabriel.biblioteca.biblioteca.repositorio;

/**
 *
 * @author gabri
 */
import com.gabriel.biblioteca.biblioteca.modelo.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestamoRepositorio extends JpaRepository<Prestamo, Long> {
    // Aquí podrías añadir filtros por usuario o libro más adelante
}
