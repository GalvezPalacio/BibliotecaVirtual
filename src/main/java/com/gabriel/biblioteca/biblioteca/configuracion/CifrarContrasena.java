/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gabriel.biblioteca.biblioteca.configuracion;

/**
 *
 * @author gabri
 */
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CifrarContrasena {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String contrasenaCifrada = encoder.encode("0002");
        System.out.println("Contraseña cifrada: " + contrasenaCifrada);
    }
}
