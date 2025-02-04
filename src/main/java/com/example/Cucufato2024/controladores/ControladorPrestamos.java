package com.example.Cucufato2024.controladores;

import com.example.Cucufato2024.dto.LineaPrestamoDto;
import com.example.Cucufato2024.dto.PrestamoDto;
import com.example.Cucufato2024.entidades.LineaPrestamo;
import com.example.Cucufato2024.entidades.Material;
import com.example.Cucufato2024.entidades.Prestamo;
import com.example.Cucufato2024.repositorios.RepositorioLineaPrestamo;
import com.example.Cucufato2024.repositorios.RepositorioMateriales;
import com.example.Cucufato2024.repositorios.RepositorioPrestamos;
import com.example.Cucufato2024.servicios.ServicioMateriales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ControladorPrestamos {
    @Autowired
    ServicioMateriales servicioMateriales;
    @Autowired
    RepositorioPrestamos repositorioPrestamos;
    @Autowired
    RepositorioLineaPrestamo repositorioLineaPrestamos;

    private static final Logger logger = LoggerFactory.getLogger(ControladorPrestamos.class);

    @GetMapping("/nuevoprestamo")
    public String nuevoPrestamo(Model model) {
        logger.info("Iniciando la creación de un nuevo préstamo.");

        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setFecha(Date.valueOf(LocalDate.now()));
        prestamoDto.setUsuario("");
        prestamoDto.setDescripcion("");

        //Recuperando materiales
        List<Material> listaMateriales = (List<Material>) servicioMateriales.findAll();
        logger.info("Número de materiales recuperados: {}", listaMateriales.size());

        for (Material material : listaMateriales) {
            LineaPrestamoDto lineaPrestamoDto = new LineaPrestamoDto();
            lineaPrestamoDto.setMaterial(material);
            lineaPrestamoDto.setFecha(Date.valueOf(LocalDate.now()));
            lineaPrestamoDto.setCantidad(material.getCantidadDisponible());
            lineaPrestamoDto.setCantidadPedida(0);
            lineaPrestamoDto.setDevuelto(false);
            lineaPrestamoDto.setPrestamoDto(prestamoDto);


            prestamoDto.getLineaPrestamoDtos().add(lineaPrestamoDto);
        }

        model.addAttribute("prestamoDto", prestamoDto);
        return "nuevoprestamo";
    }

    @PostMapping("/nuevoprestamo")
    public String nuevoPrestamo(@ModelAttribute PrestamoDto prestamoDto) {
        logger.info("Guardando un nuevo préstamo para el usuario: {}", prestamoDto.getUsuario());

        Prestamo prestamo = new Prestamo();
        prestamo.setFecha(prestamoDto.getFecha());
        prestamo.setUsuario(prestamoDto.getUsuario());
        prestamo.setDescripcion(prestamoDto.getDescripcion());
        prestamo.setActivo(true);
        repositorioPrestamos.save(prestamo);

        for (LineaPrestamoDto lineaPrestamoDto : prestamoDto.getLineaPrestamoDtos()) {
            if (lineaPrestamoDto.getCantidadPedida() > 0) {
                LineaPrestamo lineaPrestamo = new LineaPrestamo();
                lineaPrestamo.setMaterial(lineaPrestamoDto.getMaterial());
                lineaPrestamo.setFecha(lineaPrestamoDto.getFecha());
                lineaPrestamo.setCantidad(lineaPrestamoDto.getCantidadPedida());
                lineaPrestamo.setDevuelto(false);
                lineaPrestamo.setPrestamo(prestamo);

                //Registro de la linea de prestamo
                logger.debug("Guardando línea de préstamo para material: {}, Cantidad pedida: {}", lineaPrestamoDto.getMaterial().getNombre(), lineaPrestamoDto.getCantidad());

                repositorioLineaPrestamos.save(lineaPrestamo);

                Material auxMaterial = servicioMateriales.findById(lineaPrestamoDto.getMaterial().getId());
                int cantidadAnterior = auxMaterial.getCantidadDisponible();
                int cantidadPedida = lineaPrestamoDto.getCantidadPedida();

                auxMaterial.setCantidadDisponible(cantidadAnterior - cantidadPedida);
                servicioMateriales.save(auxMaterial);

                //Actualizacion de la cantidad
                logger.info("Actualización de material: {}. Cantidad antes: {}, Cantidad pedida: {}, Cantidad después: {}",
                        auxMaterial.getNombre(), cantidadAnterior, cantidadPedida, auxMaterial.getCantidadDisponible());
            }
        }

        return "redirect:/nuevoprestamo";
    }
}

