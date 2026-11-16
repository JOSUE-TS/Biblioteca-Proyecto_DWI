package com.example.biblioteca.seguridad;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtServicio {
    private final SecretKey clave;
    private final long duracion;

    public JwtServicio(@Value("${jwt.secreto}") String secreto,
                       @Value("${jwt.duracion-minutos:120}") long minutos) {
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        this.duracion = minutos * 60_000;
    }

    public String generar(String email) {
        Date ahora = new Date();
        return Jwts.builder()
                .subject(email)
                .issuedAt(ahora)
                .expiration(new Date(ahora.getTime() + duracion))
                .signWith(clave)
                .compact();
    }

    public String leerEmail(String token) {
        return Jwts.parser().verifyWith(clave).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }
}
