package com.example.biblioteca.controlador;

import com.example.biblioteca.modelo.Usuario;
import com.example.biblioteca.repositorio.UsuarioRepositorio;
import com.example.biblioteca.seguridad.JwtServicio;
import com.example.biblioteca.servicio.UsuarioServicio;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AccesoControlador {

    private final AuthenticationManager autenticador;
    private final UsuarioRepositorio usuarios;
    private final UsuarioServicio servicio;
    private final JwtServicio jwt;

    public AccesoControlador(AuthenticationManager autenticador,
                            UsuarioRepositorio usuarios,
                            UsuarioServicio servicio,
                            JwtServicio jwt) {
        this.autenticador = autenticador;
        this.usuarios = usuarios;
        this.servicio = servicio;
        this.jwt = jwt;
    }

    public record DatosAcceso(@NotBlank String email, @NotBlank String clave) { }
    public record DatosRegistro(@NotBlank String nombre, @NotBlank String apellido,
                                @Email @NotBlank String email, @NotBlank String clave) { }

    @PostMapping("/login")
    public Map<String, String> ingresar(@Valid @RequestBody DatosAcceso datos) {
        Authentication autenticacion;
        try {
            autenticacion = autenticador.authenticate(
                    new UsernamePasswordAuthenticationToken(datos.email(), datos.clave())
            );
        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
        }

        Usuario usuario = usuarios.findByEmail(autenticacion.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Credenciales incorrectas"));

        return Map.of(
                "token", jwt.generar(usuario.getEmail()),
                "rol", usuario.getRol().name()
        );
    }

    @PostMapping("/registro")
    public Usuario registrar(@Valid @RequestBody DatosRegistro datos) {
        Usuario lector = new Usuario();
        lector.setNombre(datos.nombre());
        lector.setApellido(datos.apellido());
        lector.setEmail(datos.email());
        lector.setClave(datos.clave());
        lector.setRol(Usuario.Rol.LECTOR);
        return servicio.crear(lector);
    }
}
