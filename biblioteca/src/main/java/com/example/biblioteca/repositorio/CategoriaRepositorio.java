package com.example.biblioteca.repositorio;

import com.example.biblioteca.modelo.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepositorio extends JpaRepository<Categoria, Long> { }
