package com.example.biblioteca.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String titulo;
    @NotBlank
    private String autor;
    private String editorial;
    @Column(unique = true)
    private String isbn;
    private Integer anioPublicacion;
    private boolean disponible = true;
    @Column(name = "ejemplares_totales")
    private Integer ejemplaresTotales;
    @Column(name = "ejemplares_disponibles")
    private Integer ejemplaresDisponibles;
    @Column(name = "portada_url", length = 500)
    private String portadaUrl;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public Integer getAnioPublicacion() { return anioPublicacion; }
    public void setAnioPublicacion(Integer anioPublicacion) { this.anioPublicacion = anioPublicacion; }
    public String getPortadaUrl() { return portadaUrl; }
    public void setPortadaUrl(String portadaUrl) { this.portadaUrl = portadaUrl; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    public int getEjemplaresTotales() { return ejemplaresTotales == null ? 1 : ejemplaresTotales; }
    public void setEjemplaresTotales(Integer ejemplaresTotales) { this.ejemplaresTotales = ejemplaresTotales; }
    public int getEjemplaresDisponibles() {
        return ejemplaresDisponibles == null ? (disponible ? 1 : 0) : ejemplaresDisponibles;
    }
    public void setEjemplaresDisponibles(Integer ejemplaresDisponibles) { this.ejemplaresDisponibles = ejemplaresDisponibles; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}
