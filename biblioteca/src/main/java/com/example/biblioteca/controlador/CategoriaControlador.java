package com.example.biblioteca.controlador;

import com.example.biblioteca.modelo.Categoria;
import com.example.biblioteca.servicio.CategoriaServicio;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categorias")
public class CategoriaControlador {
    private final CategoriaServicio servicio;
    public CategoriaControlador(CategoriaServicio servicio) { this.servicio = servicio; }

    @GetMapping
    public List<Categoria> listar() { return servicio.listar(); }
    @GetMapping("/{id}")
    public Categoria obtener(@PathVariable Long id) { return servicio.obtener(id); }
    @PostMapping
    public Categoria crear(@Valid @RequestBody Categoria dato) { return servicio.crear(dato); }
    @PutMapping("/{id}")
    public Categoria actualizar(@PathVariable Long id, @Valid @RequestBody Categoria dato) {
        return servicio.actualizar(id, dato);
    }
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) { servicio.eliminar(id); }
}
