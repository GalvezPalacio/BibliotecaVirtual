package com.gabriel.biblioteca.biblioteca.controlador;

import com.gabriel.biblioteca.biblioteca.modelo.Usuario;
import com.gabriel.biblioteca.biblioteca.modelo.UsuarioAutenticado;
import com.gabriel.biblioteca.biblioteca.modelo.UsuarioDTO;
import com.gabriel.biblioteca.biblioteca.repositorio.UsuarioRepositorio;
import com.gabriel.biblioteca.biblioteca.servicio.UsuarioDetalles;
import com.gabriel.biblioteca.biblioteca.servicio.UsuarioServicio;
import jakarta.validation.Valid;
import java.util.HashMap;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioControlador {

    private final UsuarioServicio usuarioServicio;
    private final UsuarioRepositorio usuarioRepositorio; // ✅ Añadir esta línea

    public UsuarioControlador(UsuarioServicio usuarioServicio, UsuarioRepositorio usuarioRepositorio) { // ✅ Modificar constructor
        this.usuarioServicio = usuarioServicio;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @GetMapping
    public List<Usuario> obtenerTodos() {
        return usuarioServicio.obtenerTodos();
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody Usuario usuario) {
        Usuario guardado = usuarioServicio.guardarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @GetMapping("/privado")
    @PreAuthorize("hasRole('USUARIO')")
    public String zonaPrivadaUsuario() {
        return "Bienvenido, usuario. Acceso autorizado.";
    }

    @GetMapping("/rol")
    public Map<String, String> obtenerRol(@AuthenticationPrincipal UsuarioDetalles detalles) {
        if (detalles != null && detalles.getUsuario() != null) {
            return Map.of("rol", detalles.getUsuario().getTipo());
        } else {
            return Map.of("rol", "desconocido");
        }
    }

    @GetMapping("/yo")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioAutenticado(Authentication authentication) {
        String email = authentication.getName();
        UsuarioDTO dto = usuarioServicio.obtenerUsuarioActual(email);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/usuarios/todos")
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public ResponseEntity<?> obtenerTodosLosUsuariosConLibros() {
        List<Usuario> usuarios = usuarioRepositorio.findAll();

        List<Map<String, Object>> resultado = usuarios.stream().map(u -> {
            Map<String, Object> info = new HashMap<>();
            info.put("nombre", u.getNombre());
            info.put("email", u.getEmail());
            info.put("tipo", u.getTipo());

            List<String> librosAlquilados = u.getAlquileres().stream()
                    .filter(a -> a.getFechaDevolucion() == null)
                    .map(a -> a.getLibro().getTitulo() + " (ID: " + a.getLibro().getId() + ")")
                    .toList();

            info.put("libros", librosAlquilados);
            return info;
        }).toList();

        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/{email}")
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable String email) {
        Usuario usuario = usuarioRepositorio.findByEmail(email).orElse(null);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        usuarioRepositorio.delete(usuario);
        return ResponseEntity.noContent().build();
    }
}
