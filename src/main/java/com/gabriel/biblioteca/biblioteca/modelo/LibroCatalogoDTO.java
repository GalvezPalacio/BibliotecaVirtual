/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gabriel.biblioteca.biblioteca.modelo;

/**
 *
 * @author gabri
 */
public class LibroCatalogoDTO {

    private Long id;
    private String titulo;
    private int anio;
    private boolean disponible;
    private String nombreUsuario;

    // Constructor vacío
    public LibroCatalogoDTO() {
    }

    // Constructor completo
    public LibroCatalogoDTO(Long id, String titulo, int anio, boolean disponible, String nombreUsuario) {
        this.id = id;
        this.titulo = titulo;
        this.anio = anio;
        this.disponible = disponible;
        this.nombreUsuario = nombreUsuario;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}
