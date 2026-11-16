package com.example.biblioteca.repositorio;

import com.example.biblioteca.modelo.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LibroRepositorio extends JpaRepository<Libro, Long> {
    // Las actualizaciones JPQL se realizan dentro de una transacción de préstamo.
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Libro l set l.ejemplaresDisponibles = coalesce(l.ejemplaresDisponibles, case when l.disponible = true then 1 else 0 end) - 1, l.disponible = case when coalesce(l.ejemplaresDisponibles, case when l.disponible = true then 1 else 0 end) > 1 then true else false end where l.id = :id and coalesce(l.ejemplaresDisponibles, case when l.disponible = true then 1 else 0 end) > 0")
    int ocupar(@Param("id") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Libro l set l.ejemplaresDisponibles = coalesce(l.ejemplaresDisponibles, case when l.disponible = true then 1 else 0 end) + 1, l.disponible = true where l.id = :id")
    int liberar(@Param("id") Long id);
}
