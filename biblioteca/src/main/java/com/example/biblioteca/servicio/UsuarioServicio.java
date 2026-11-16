package com.example.biblioteca.servicio;

import com.example.biblioteca.modelo.Usuario;
import com.example.biblioteca.repositorio.UsuarioRepositorio;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioServicio {
    private final UsuarioRepositorio usuarios;
    private final PasswordEncoder claves;

    public UsuarioServicio(UsuarioRepositorio usuarios, PasswordEncoder claves) {
        this.usuarios = usuarios;
        this.claves = claves;
    }

    public List<Usuario> listar() { return usuarios.findAll(); }

    public Usuario obtener(Long id) {
        return usuarios.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    public Usuario crear(Usuario dato) {
        if (dato.getClave() == null || dato.getClave().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La clave es obligatoria");
        }
        dato.setId(null);
        if (dato.getCodigo() == null || dato.getCodigo().isBlank()) {
            dato.setCodigo(generarCodigoLector());
        }
        dato.setClave(claves.encode(dato.getClave()));
        if (dato.getRol() == null) dato.setRol(Usuario.Rol.LECTOR);
        return usuarios.save(dato);
    }

    private String generarCodigoLector() {
        String codigo;
        do {
            codigo = "LEC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (usuarios.existsByCodigo(codigo));
        return codigo;
    }

    public Usuario actualizar(Long id, Usuario dato) {
        Usuario actual = obtener(id);
        actual.setNombre(dato.getNombre());
        actual.setApellido(dato.getApellido());
        actual.setCodigo(dato.getCodigo());
        actual.setEmail(dato.getEmail());
        if (dato.getRol() != null) actual.setRol(dato.getRol());
        if (dato.getClave() != null && !dato.getClave().isBlank()) {
            actual.setClave(claves.encode(dato.getClave()));
        }
        return usuarios.save(actual);
    }

    public void eliminar(Long id) {
        usuarios.delete(obtener(id));
    }
}
