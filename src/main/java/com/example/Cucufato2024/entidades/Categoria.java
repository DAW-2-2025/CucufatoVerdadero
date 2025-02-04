package com.example.Cucufato2024.entidades;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nombre;
    @Column(columnDefinition = "text")
    private String descripcion;
}
