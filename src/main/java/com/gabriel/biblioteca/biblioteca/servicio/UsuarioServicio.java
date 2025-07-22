/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gabriel.biblioteca.biblioteca.servicio;

/**
 *
 * @author gabri
 */
import com.gabriel.biblioteca.biblioteca.modelo.Usuario;
import com.gabriel.biblioteca.biblioteca.modelo.UsuarioDTO;
import com.gabriel.biblioteca.biblioteca.repositorio.UsuarioRepositorio;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServicio {

    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio, PasswordEncoder passwordEncoder) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepositorio.findAll();
    }

    public Usuario guardarUsuario(Usuario usuario) {
        // ✅ Cifrar contraseña antes de guardar
        String contrasenaCifrada = passwordEncoder.encode(usuario.getContrasena());
        usuario.setContrasena(contrasenaCifrada);
        return usuarioRepositorio.save(usuario);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepositorio.findById(id).orElse(null);
    }

    public boolean eliminarUsuario(Long id) {
        if (usuarioRepositorio.existsById(id)) {
            usuarioRepositorio.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public UsuarioDTO obtenerUsuarioActual(String email) {
        Usuario usuario = usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return new UsuarioDTO(usuario.getId(), usuario.getNombre(), usuario.getTipo());
    }
}
