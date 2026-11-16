package com.example.biblioteca.servicio;

import com.example.biblioteca.modelo.Prestamo;
import com.example.biblioteca.repositorio.LibroRepositorio;
import com.example.biblioteca.repositorio.PrestamoRepositorio;
import com.example.biblioteca.repositorio.UsuarioRepositorio;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PrestamoServicio {
    private final PrestamoRepositorio prestamos;
    private final LibroRepositorio libros;
    private final UsuarioRepositorio usuarios;

    public PrestamoServicio(PrestamoRepositorio prestamos, LibroRepositorio libros,
                            UsuarioRepositorio usuarios) {
        this.prestamos = prestamos;
        this.libros = libros;
        this.usuarios = usuarios;
    }

    public List<Prestamo> listar() { return prestamos.findAll(); }
    public List<Prestamo> listarPorUsuario(Long usuarioId) {
        return prestamos.buscarPorUsuario(usuarioId);
    }

    public Prestamo obtener(Long id) {
        return prestamos.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Préstamo no encontrado"));
    }

    @Transactional
    public Prestamo crear(Long libroId, Long usuarioId, LocalDate fechaEstimada, String observacion) {
        if (fechaEstimada == null || fechaEstimada.isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha estimada no es válida");
        }
        var usuario = usuarios.findById(usuarioId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        if (libros.ocupar(libroId) != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Libro no disponible o inexistente");
        }
        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libros.findById(libroId).orElseThrow());
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaEstimada(fechaEstimada);
        prestamo.setObservacion(observacion);
        return prestamos.save(prestamo);
    }

    @Transactional
    public Prestamo actualizar(Long id, LocalDate fechaEstimada, String observacion) {
        Prestamo prestamo = obtener(id);
        if (fechaEstimada == null || fechaEstimada.isBefore(prestamo.getFechaPrestamo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha estimada no es válida");
        }
        prestamo.setFechaEstimada(fechaEstimada);
        prestamo.setObservacion(observacion);
        return prestamos.save(prestamo);
    }

    @Transactional
    public Prestamo devolver(Long id) {
        Prestamo prestamo = obtener(id);
        if (prestamo.getFechaDevolucion() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El libro ya fue devuelto");
        }
        prestamo.setFechaDevolucion(LocalDate.now());
        libros.liberar(prestamo.getLibro().getId());
        return prestamos.save(prestamo);
    }

    @Transactional
    public void eliminar(Long id) {
        Prestamo prestamo = obtener(id);
        if (prestamo.getFechaDevolucion() == null) {
            libros.liberar(prestamo.getLibro().getId());
        }
        prestamos.delete(prestamo);
    }
}
