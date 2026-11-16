package com.example.biblioteca.controlador;

import com.example.biblioteca.modelo.Libro;
import com.example.biblioteca.servicio.LibroServicio;
import com.example.biblioteca.servicio.AlmacenPortadas;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/libros")
public class LibroControlador {
    private final LibroServicio servicio;
    private final AlmacenPortadas portadas;
    public LibroControlador(LibroServicio servicio, AlmacenPortadas portadas) {
        this.servicio = servicio;
        this.portadas = portadas;
    }

    @GetMapping
    public List<Libro> listar() { return servicio.listar(); }
    @GetMapping("/{id}")
    public Libro obtener(@PathVariable Long id) { return servicio.obtener(id); }
    @PostMapping
    public Libro crear(@Valid @RequestBody Libro dato) { return servicio.crear(dato); }
    @PutMapping("/{id}")
    public Libro actualizar(@PathVariable Long id, @Valid @RequestBody Libro dato) {
        return servicio.actualizar(id, dato);
    }

    @PostMapping(value = "/{id}/portada", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Libro subirPortada(@PathVariable Long id, @RequestPart("archivo") MultipartFile archivo) {
        Libro libro = servicio.obtener(id);
        String portadaAnterior = libro.getPortadaUrl();
        String portadaNueva = portadas.guardar(archivo);
        Libro actualizado;
        try {
            actualizado = servicio.actualizarPortada(id, portadaNueva);
        } catch (RuntimeException ex) {
            portadas.eliminar(portadaNueva);
            throw ex;
        }
        portadas.eliminar(portadaAnterior);
        return actualizado;
    }

    @DeleteMapping("/{id}/portada")
    public Libro quitarPortada(@PathVariable Long id) {
        Libro libro = servicio.obtener(id);
        Libro actualizado = servicio.actualizarPortada(id, null);
        portadas.eliminar(libro.getPortadaUrl());
        return actualizado;
    }
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        Libro libro = servicio.obtener(id);
        servicio.eliminar(id);
        portadas.eliminar(libro.getPortadaUrl());
    }
}
