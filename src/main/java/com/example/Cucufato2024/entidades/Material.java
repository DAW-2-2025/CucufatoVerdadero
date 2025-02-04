package com.example.Cucufato2024.entidades;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nombre;
    @ManyToOne
    Categoria categoria;
    private String departamento;
    private int cantidadTotal;
    private int cantidadDisponible;
    private int noAveriados;
    @Column(columnDefinition = "text")
    private String falta;
    @Column(columnDefinition = "text")
    private String rotos;
    private String codigoBarras;
    private String imagen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa; // Relación con Empresa para Multipropiedad
    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area area;
}
