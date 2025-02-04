package com.example.Cucufato2024.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Data
public class LineaPrestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Material material;

    private int cantidad;

    // Con JsonIgnore, al consultar un Prestamo, los hijos LineaPrestamo de este no tendrán un hijo Prestamo que referencie a su padre. Así se evita anidamiento cíclico.
    @ManyToOne
    @JsonIgnore
    private Prestamo prestamo;

    private boolean devuelto;
    private Date fecha;
}
