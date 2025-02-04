package com.example.Cucufato2024.servicios.impl;

import com.example.Cucufato2024.dto.LineaPrestamoDto;
import com.example.Cucufato2024.dto.PrestamoDto;
import com.example.Cucufato2024.entidades.LineaPrestamo;
import com.example.Cucufato2024.entidades.Material;
import com.example.Cucufato2024.entidades.Prestamo;
import com.example.Cucufato2024.repositorios.RepositorioLineaPrestamo;
import com.example.Cucufato2024.repositorios.RepositorioPrestamos;
import com.example.Cucufato2024.servicios.ServicioMateriales;
import com.example.Cucufato2024.servicios.ServicioPrestamo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioPrestamoImpl implements ServicioPrestamo {
    @Autowired
    ServicioMateriales servicioMateriales;
    @Autowired
    RepositorioPrestamos repositorioPrestamos;
    @Autowired
    RepositorioLineaPrestamo repositorioLineaPrestamos;

    @Override
    public PrestamoDto crearPrestamoDto() {
        //Creo un DTO de préstamo vacío y le pongo la fecha actual
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setFecha(Date.valueOf(LocalDate.now()));
        prestamoDto.setUsuario("");
        prestamoDto.setDescripcion("");

        //Recupero en una lista todos los materiales de nuestro sistema
        ArrayList<Material> listaMateriales = (ArrayList<Material>) servicioMateriales.findAll();

        //Cada uno de esos materiales los añado como una líneaPrestamoDTO en el prestamoDTO
        for (Material material : listaMateriales) {
            //Creo una lineaPrestamoDTO vacía
            LineaPrestamoDto lineaPrestamoDto = new LineaPrestamoDto();
            //En la propiedad material le pongo el material de esta iteracion del bucle
            lineaPrestamoDto.setMaterial(material);
            //Le pongo la fecha actual
            lineaPrestamoDto.setFecha(Date.valueOf(LocalDate.now()));
            //Como cantidad le asigno la cantidad disponible del material correspondiente
            lineaPrestamoDto.setCantidad(material.getCantidadDisponible());
            //Inicialmente no se lleva ninguno, ya elegirá los que quiere
            lineaPrestamoDto.setCantidadPedida(0);
            //Si no se los ha llevado, es imposible que los devuelva
            lineaPrestamoDto.setDevuelto(false);
            //Esta línea se asigna al prestamoDTO que hemos creado al principio de este método
            lineaPrestamoDto.setPrestamoDto(prestamoDto);
            //tengo que añadir esta lineaPrestamoDto al préstamo que he creado antes
            prestamoDto.getLineaPrestamoDtos().add(lineaPrestamoDto);
        }
        return prestamoDto;
    }

    @Override
    public Prestamo guardarDto(PrestamoDto prestamoDto) {
        //NO solo convierte, también "guarda" el préstamo en la BBDD
        Prestamo prestamo = new Prestamo();
        prestamo.setFecha(prestamoDto.getFecha());
        prestamo.setUsuario(prestamoDto.getUsuario());
        prestamo.setDescripcion(prestamoDto.getDescripcion());
        prestamo.setActivo(true);
        repositorioPrestamos.save(prestamo);

        for(LineaPrestamoDto lineaPrestamoDto : prestamoDto.getLineaPrestamoDtos()){
            LineaPrestamo lineaPrestamo = new LineaPrestamo();

            lineaPrestamo.setMaterial(lineaPrestamoDto.getMaterial());
            lineaPrestamo.setFecha(lineaPrestamoDto.getFecha());
            lineaPrestamo.setCantidad(lineaPrestamoDto.getCantidadPedida());
            lineaPrestamo.setDevuelto(false);
            //Es necesario que previamente haga el save de este préstamo
            lineaPrestamo.setPrestamo(prestamo);

            //Solo guardo la línea si ha pedido algo de este material.
            if(lineaPrestamoDto.getCantidadPedida()>0) {
                repositorioLineaPrestamos.save(lineaPrestamo);

                //Ahora me toca actualizar la cantidad disponible del material
                Material auxMaterial = servicioMateriales.findById(lineaPrestamoDto.getMaterial().getId());
                int disponible = auxMaterial.getCantidadDisponible();
                int pedido = lineaPrestamoDto.getCantidadPedida();

                auxMaterial.setCantidadDisponible(disponible - pedido);
                servicioMateriales.save(auxMaterial);
            }
        }

        return prestamo;
    }


    public List<Prestamo> obtenerPrestamosActivos() {
        return repositorioPrestamos.findByActivoTrue();
    }

    public double calcularTasaMorosidad() {
        long totalPrestamos = repositorioPrestamos.contarTodos();
        long prestamosMorosos = repositorioPrestamos.contarPorMorosidad();
        return (totalPrestamos == 0) ? 0 : (double) prestamosMorosos / totalPrestamos * 100;
    }

    public double calcularTasaResolucion() {
        long prestamosResueltos = repositorioPrestamos.contarResueltos();
        long totalPrestamos = repositorioPrestamos.contarTodos();
        return (totalPrestamos == 0) ? 0 : (double) prestamosResueltos / totalPrestamos * 100;
    }
}
