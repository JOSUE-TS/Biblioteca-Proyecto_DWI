package com.example.biblioteca.seguridad;

import com.example.biblioteca.modelo.Usuario;
import com.example.biblioteca.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatosIniciales {
    @Bean
    CommandLineRunner crearAdministrador(UsuarioRepositorio usuarios, PasswordEncoder encoder,
                  @Value("${biblioteca.admin.email}") String email,
                  @Value("${biblioteca.admin.clave}") String clave) {
        return args -> {
            if (usuarios.findByEmail(email).isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombre("Administrador");
                admin.setApellido("Biblioteca");
                admin.setCodigo("ADMIN001");
                admin.setEmail(email);
                admin.setClave(encoder.encode(clave));
                admin.setRol(Usuario.Rol.ADMIN);
                usuarios.save(admin);
            }
        };
    }
}
