package com.example.biblioteca.seguridad;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SeguridadConfiguracion {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UsuarioDetallesServicio detallesServicio,
                                                       PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider proveedor = new DaoAuthenticationProvider(detallesServicio);
        proveedor.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(proveedor);
    }

    @Bean
    public SecurityFilterChain filtro(HttpSecurity http,
                                     JwtServicio jwt,
                                     UsuarioDetallesServicio detallesServicio,
                                     AuthenticationManager authenticationManager) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(authenticationManager)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/registro").permitAll()
                        .requestMatchers(HttpMethod.GET, "/libros", "/libros/**", "/categorias", "/categorias/**")
                        .hasAnyRole("ADMIN", "LECTOR")
                        .requestMatchers(HttpMethod.GET, "/prestamos/mios").hasAnyRole("ADMIN", "LECTOR")
                        .requestMatchers(HttpMethod.POST, "/prestamos/solicitar").hasAnyRole("ADMIN", "LECTOR")
                        .requestMatchers("/uploads/portadas/**").permitAll()
                        .requestMatchers("/libros", "/libros/**", "/categorias", "/categorias/**",
                                "/usuarios", "/usuarios/**", "/prestamos", "/prestamos/**")
                        .hasRole("ADMIN")
                        .anyRequest().authenticated())
                .exceptionHandling(errores -> errores
                        .authenticationEntryPoint((req, res, ex) -> res.sendError(401, "No autorizado"))
                        .accessDeniedHandler((req, res, ex) -> res.sendError(403, "Sin permiso")))
                .addFilterBefore(new JwtFiltro(jwt, detallesServicio),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
