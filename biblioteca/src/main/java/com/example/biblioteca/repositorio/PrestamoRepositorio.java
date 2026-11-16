package com.example.biblioteca.repositorio;

import com.example.biblioteca.modelo.Prestamo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PrestamoRepositorio extends JpaRepository<Prestamo, Long> {
    @Query("select p from Prestamo p where p.usuario.id = :usuarioId order by p.fechaPrestamo desc")
    List<Prestamo> buscarPorUsuario(@Param("usuarioId") Long usuarioId);
}
