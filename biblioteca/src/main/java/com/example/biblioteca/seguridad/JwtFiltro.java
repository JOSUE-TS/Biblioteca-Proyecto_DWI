package com.example.biblioteca.seguridad;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtFiltro extends OncePerRequestFilter {

    private final JwtServicio jwt;
    private final UsuarioDetallesServicio detallesServicio;

    public JwtFiltro(JwtServicio jwt, UsuarioDetallesServicio detallesServicio) {
        this.jwt = jwt;
        this.detallesServicio = detallesServicio;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String autorizacion = request.getHeader("Authorization");

        if (autorizacion != null && autorizacion.startsWith("Bearer ")
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                String token = autorizacion.substring(7);
                // leerEmail verifica la firma y la expiracion del JWT.
                String email = jwt.leerEmail(token);
                UserDetails usuario = detallesServicio.loadUserByUsername(email);

                if (email.equals(usuario.getUsername())) {
                    var autenticacion = new UsernamePasswordAuthenticationToken(
                            usuario, null, usuario.getAuthorities());
                    autenticacion.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(autenticacion);
                }
            } catch (JwtException | IllegalArgumentException | UsernameNotFoundException ex) {
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(request, response);
    }
}
