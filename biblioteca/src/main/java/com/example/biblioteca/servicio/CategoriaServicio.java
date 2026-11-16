package com.example.biblioteca.servicio;

import com.example.biblioteca.modelo.Categoria;
import com.example.biblioteca.repositorio.CategoriaRepositorio;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CategoriaServicio {
    private final CategoriaRepositorio repositorio;

    public CategoriaServicio(CategoriaRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public List<Categoria> listar() { return repositorio.findAll(); }

    public Categoria obtener(Long id) {
        return repositorio.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));
    }

    public Categoria crear(Categoria dato) {
        dato.setId(null);
        return repositorio.save(dato);
    }

    public Categoria actualizar(Long id, Categoria dato) {
        Categoria actual = obtener(id);
        actual.setNombre(dato.getNombre());
        actual.setDescripcion(dato.getDescripcion());
        return repositorio.save(actual);
    }

    public void eliminar(Long id) {
        repositorio.delete(obtener(id));
    }
}
