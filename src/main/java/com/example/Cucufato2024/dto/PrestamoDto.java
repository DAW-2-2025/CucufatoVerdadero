package com.example.Cucufato2024.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
@Data
public class PrestamoDto {

    private long id;

    private String usuario;
    private Date fecha;
    private Date fechaDevolucion;
    private boolean activo;
    private String descripcion;

    List<LineaPrestamoDto> lineaPrestamoDtos;

    public PrestamoDto() {
        lineaPrestamoDtos = new ArrayList<>();
    }
}
