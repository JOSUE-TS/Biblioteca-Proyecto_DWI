package com.example.biblioteca.controlador;

import com.example.biblioteca.modelo.Prestamo;
import com.example.biblioteca.servicio.PrestamoServicio;
import com.example.biblioteca.repositorio.UsuarioRepositorio;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.security.Principal;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prestamos")
public class PrestamoControlador {
    private final PrestamoServicio servicio;
    private final UsuarioRepositorio usuarios;
    public PrestamoControlador(PrestamoServicio servicio, UsuarioRepositorio usuarios) {
        this.servicio = servicio;
        this.usuarios = usuarios;
    }

    public record NuevoPrestamo(@NotNull Long libroId, @NotNull Long usuarioId,
                                @NotNull LocalDate fechaEstimada, String observacion) { }
    public record CambioPrestamo(@NotNull LocalDate fechaEstimada, String observacion) { }
    public record SolicitudPrestamo(@NotNull Long libroId, @NotNull LocalDate fechaEstimada,
                                    String observacion) { }

    @GetMapping("/mios")
    public List<Prestamo> misPrestamos(Principal principal) {
        var usuario = usuarios.findByEmail(principal.getName()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        return servicio.listarPorUsuario(usuario.getId());
    }

    @PostMapping("/solicitar")
    public Prestamo solicitar(@Valid @RequestBody SolicitudPrestamo dato, Principal principal) {
        var usuario = usuarios.findByEmail(principal.getName()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        return servicio.crear(dato.libroId(), usuario.getId(), dato.fechaEstimada(), dato.observacion());
    }

    @GetMapping
    public List<Prestamo> listar(@RequestParam(required = false) Long usuarioId) {
        return usuarioId == null ? servicio.listar() : servicio.listarPorUsuario(usuarioId);
    }
    @GetMapping("/{id}")
    public Prestamo obtener(@PathVariable Long id) { return servicio.obtener(id); }
    @PostMapping
    public Prestamo crear(@Valid @RequestBody NuevoPrestamo dato) {
        return servicio.crear(dato.libroId(), dato.usuarioId(), dato.fechaEstimada(), dato.observacion());
    }
    @PutMapping("/{id}")
    public Prestamo actualizar(@PathVariable Long id, @Valid @RequestBody CambioPrestamo dato) {
        return servicio.actualizar(id, dato.fechaEstimada(), dato.observacion());
    }
    @PutMapping("/{id}/devolver")
    public Prestamo devolver(@PathVariable Long id) { return servicio.devolver(id); }
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) { servicio.eliminar(id); }
}
