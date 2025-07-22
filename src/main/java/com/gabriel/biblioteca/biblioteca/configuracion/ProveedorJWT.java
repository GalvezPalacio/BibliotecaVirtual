/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gabriel.biblioteca.biblioteca.configuracion;

/**
 *
 * @author gabri
 */
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class ProveedorJWT {

    private final String CLAVE_SECRETA = "1234567890123456789012345678901234567890123456789012345678901234";

    private Key obtenerClave() {
        return Keys.hmacShaKeyFor(CLAVE_SECRETA.getBytes());
    }

    public String extraerUsuario(String token) {
        return extraerDato(token, Claims::getSubject);
    }

    public Date extraerFechaExpiracion(String token) {
        return extraerDato(token, Claims::getExpiration);
    }

    public <T> T extraerDato(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extraerTodosLosClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extraerTodosLosClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(obtenerClave())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean tokenExpirado(String token) {
        return extraerFechaExpiracion(token).before(new Date());
    }

    public String generarToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return crearToken(claims, userDetails.getUsername());
    }

    private String crearToken(Map<String, Object> claims, String subject) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(obtenerClave(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean esTokenValido(String token, UserDetails userDetails) {
        final String username = extraerUsuario(token);
        return (username.equals(userDetails.getUsername()) && !tokenExpirado(token));
    }
}
