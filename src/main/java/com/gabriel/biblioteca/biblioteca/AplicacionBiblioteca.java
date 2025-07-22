package com.gabriel.biblioteca.biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity
@SpringBootApplication
public class AplicacionBiblioteca {

    public static void main(String[] args) {
        SpringApplication.run(AplicacionBiblioteca.class, args);
        System.out.println("Aplicacion Spring Boot iniciada con exito");
    }
}
