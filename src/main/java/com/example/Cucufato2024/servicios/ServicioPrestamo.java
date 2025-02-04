package com.example.Cucufato2024.servicios;

import java.util.List;

import com.example.Cucufato2024.dto.PrestamoDto;
import com.example.Cucufato2024.entidades.Prestamo;

public interface ServicioPrestamo {
    public PrestamoDto crearPrestamoDto();  //Crea un préstamoDto a partir de todos los materiales
    public Prestamo guardarDto(PrestamoDto prestamoDto);

    public List<Prestamo> obtenerPrestamosActivos();
    public double calcularTasaMorosidad();
    public double calcularTasaResolucion();
}
