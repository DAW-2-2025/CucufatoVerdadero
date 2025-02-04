package com.example.Cucufato2024.entidades;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Entity
@Data
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String usuario;
    private Date fecha;
    private Date fechaDevolucion;
    private boolean activo;
    @Column(columnDefinition = "text")
    private String descripcion;

    @OneToMany(mappedBy = "prestamo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LineaPrestamo> lineas_prestamo;
}
