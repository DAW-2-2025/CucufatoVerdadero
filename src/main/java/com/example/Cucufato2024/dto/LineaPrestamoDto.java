package com.example.Cucufato2024.dto;

import com.example.Cucufato2024.entidades.Material;
import com.example.Cucufato2024.entidades.Prestamo;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.sql.Date;
@Data
public class LineaPrestamoDto {
    private long id;

    private Material material;

    private int cantidad; //Esta es la cantidad disponible del material, solo es INFORMATIVO
    private int cantidadPedida;

    private PrestamoDto prestamoDto;

    private boolean devuelto;
    private Date fecha;
}
