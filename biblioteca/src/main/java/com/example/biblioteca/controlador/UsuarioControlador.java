package com.example.biblioteca.controlador;

import com.example.biblioteca.modelo.Usuario;
import com.example.biblioteca.servicio.UsuarioServicio;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControlador {
    private final UsuarioServicio servicio;
    public UsuarioControlador(UsuarioServicio servicio) { this.servicio = servicio; }

    @GetMapping
    public List<Usuario> listar() { return servicio.listar(); }
    @GetMapping("/{id}")
    public Usuario obtener(@PathVariable Long id) { return servicio.obtener(id); }
    @PostMapping
    public Usuario crear(@Valid @RequestBody Usuario dato) { return servicio.crear(dato); }
    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable Long id, @Valid @RequestBody Usuario dato) {
        return servicio.actualizar(id, dato);
    }
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) { servicio.eliminar(id); }
}
