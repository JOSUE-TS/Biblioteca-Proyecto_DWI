package com.example.biblioteca.servicio;

import com.example.biblioteca.modelo.Libro;
import com.example.biblioteca.repositorio.CategoriaRepositorio;
import com.example.biblioteca.repositorio.LibroRepositorio;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LibroServicio {
    private final LibroRepositorio libros;
    private final CategoriaRepositorio categorias;

    public LibroServicio(LibroRepositorio libros, CategoriaRepositorio categorias) {
        this.libros = libros;
        this.categorias = categorias;
    }

    public List<Libro> listar() { return libros.findAll(); }

    public Libro obtener(Long id) {
        return libros.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));
    }

    private void verificarCategoria(Libro libro) {
        if (libro.getCategoria() != null) {
            Long id = libro.getCategoria().getId();
            if (id == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta el ID de la categoría");
            libro.setCategoria(categorias.findById(id).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada")));
        }
    }

    public Libro crear(Libro dato) {
        dato.setId(null);
        int cantidad = dato.getEjemplaresTotales();
        if (cantidad < 1) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe registrar al menos un ejemplar");
        dato.setEjemplaresTotales(cantidad);
        dato.setEjemplaresDisponibles(cantidad);
        dato.setDisponible(true);
        verificarCategoria(dato);
        return libros.save(dato);
    }

    public Libro actualizar(Long id, Libro dato) {
        Libro actual = obtener(id);
        verificarCategoria(dato);
        actual.setTitulo(dato.getTitulo());
        actual.setAutor(dato.getAutor());
        actual.setEditorial(dato.getEditorial());
        actual.setIsbn(dato.getIsbn());
        actual.setAnioPublicacion(dato.getAnioPublicacion());
        actual.setCategoria(dato.getCategoria());
        if (dato.getEjemplaresTotales() > 0) {
            int prestados = actual.getEjemplaresTotales() - actual.getEjemplaresDisponibles();
            if (dato.getEjemplaresTotales() < prestados) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No puede reducir el inventario por debajo de los ejemplares prestados");
            }
            actual.setEjemplaresTotales(dato.getEjemplaresTotales());
            actual.setEjemplaresDisponibles(dato.getEjemplaresTotales() - prestados);
            actual.setDisponible(actual.getEjemplaresDisponibles() > 0);
        }
        return libros.save(actual);
    }

    public Libro actualizarPortada(Long id, String portadaUrl) {
        Libro actual = obtener(id);
        actual.setPortadaUrl(portadaUrl);
        return libros.save(actual);
    }

    public void eliminar(Long id) {
        libros.delete(obtener(id));
    }
}
