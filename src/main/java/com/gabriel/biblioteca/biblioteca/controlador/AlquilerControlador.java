/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gabriel.biblioteca.biblioteca.controlador;

import com.gabriel.biblioteca.biblioteca.modelo.Alquiler;
import com.gabriel.biblioteca.biblioteca.modelo.AlquilerPedidoDTO;
import com.gabriel.biblioteca.biblioteca.modelo.Usuario;
import com.gabriel.biblioteca.biblioteca.repositorio.AlquilerRepositorio;
import com.gabriel.biblioteca.biblioteca.servicio.AlquilerServicio;
import com.gabriel.biblioteca.biblioteca.servicio.UsuarioDetalles;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alquileres")
public class AlquilerControlador {

    private final AlquilerRepositorio alquilerRepositorio;
    private final AlquilerServicio alquilerServicio;

    public AlquilerControlador(AlquilerRepositorio alquilerRepositorio,
            AlquilerServicio alquilerServicio) {
        this.alquilerRepositorio = alquilerRepositorio;
        this.alquilerServicio = alquilerServicio;
    }

    @PostMapping
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<?> alquilarLibro(@RequestBody AlquilerPedidoDTO pedido,
            @AuthenticationPrincipal UsuarioDetalles usuarioDetalles) {
        try {
            Usuario usuario = usuarioDetalles.getUsuario();
            alquilerServicio.alquilarLibro(usuario.getEmail(), pedido.getIdLibro());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/mios")
    @PreAuthorize("hasRole('USUARIO')")
    public List<Alquiler> obtenerMisAlquileres(@AuthenticationPrincipal UsuarioDetalles usuarioDetalles) {
        return alquilerRepositorio.findByUsuarioId(usuarioDetalles.getUsuario().getId());
    }

    @PutMapping("/{id}/devolver")
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<?> devolverLibro(@PathVariable Long id) {
        try {
            alquilerServicio.devolverLibro(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/todos")
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public ResponseEntity<?> obtenerTodos() {
        try {
            List<Alquiler> alquileres = alquilerRepositorio.findAll();

            List<Map<String, String>> resultado = alquileres.stream()
                    .filter(a -> a.getFechaDevolucion() == null) // ⚠️ SOLO si no han sido devueltos
                    .map(a -> {
                        Map<String, String> item = new HashMap<>();
                        item.put("libro", a.getLibro().getTitulo());
                        item.put("usuario", a.getUsuario().getNombre());
                        return item;
                    }).toList();

            System.out.println("📚 Alquileres activos: " + resultado.size());
            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error interno al obtener alquileres");
        }
    }
}
