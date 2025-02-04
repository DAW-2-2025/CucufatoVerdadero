package com.example.Cucufato2024.controladores;

import com.example.Cucufato2024.dto.LineaPrestamoDto;
import com.example.Cucufato2024.dto.PrestamoDto;
import com.example.Cucufato2024.entidades.Material;
import com.example.Cucufato2024.repositorios.RepositorioLineaPrestamo;
import com.example.Cucufato2024.repositorios.RepositorioMateriales;
import com.example.Cucufato2024.repositorios.RepositorioPrestamos;
import com.example.Cucufato2024.servicios.ServicioMateriales;
import com.example.Cucufato2024.servicios.ServicioPrestamo;
import com.example.Cucufato2024.servicios.impl.ServicioCarritoImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sound.sampled.Line;
import java.sql.Date;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ControladorPrestamoCarrito {
    @Autowired
    ServicioMateriales servicioMateriales;
    @Autowired
    RepositorioPrestamos repositorioPrestamos;
    @Autowired
    RepositorioLineaPrestamo repositorioLineaPrestamos;
    @Autowired
    ServicioCarritoImpl servicioCarrito;
    @Autowired
    ServicioPrestamo servicioPrestamo;
    @Autowired
    private RepositorioMateriales repositorioMateriales;
    @Autowired
    private DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration;

    private static final Logger logger = LoggerFactory.getLogger(ControladorPrincipal.class);


    @GetMapping("/prestamocarrito")
    public String verMateriales(Model model) {
        logger.info("Mostrando materiales para agregar al carrito.");

        model.addAttribute("prestamoDto", servicioPrestamo.crearPrestamoDto());
        return "prestamocarrito";
    }

    @PostMapping("/addcarrito")
    public String addCarrito(@RequestParam("idMaterial") long idMaterial, @RequestParam int cantidadPedida, HttpSession session) {
        logger.info("Agregando material con id: {} al carrito. Cantidad seleccionada: {}", idMaterial, cantidadPedida);

        //Desde el formulario nos llega el id del material que piden y la cantidad
        LineaPrestamoDto lineaPrestamoDto = new LineaPrestamoDto();
        lineaPrestamoDto.setCantidadPedida(cantidadPedida);
        lineaPrestamoDto.setMaterial(repositorioMateriales.findById(idMaterial));

        // Si la cantidad pedida es mayor a 0, agregamos el artículo al carrito
        if (lineaPrestamoDto.getCantidadPedida() > 0) {
            servicioCarrito.addItem(lineaPrestamoDto, session);
        }

        return "redirect:/prestamocarrito";
    }

    @GetMapping("/mostrarcarrito")
    public String mostrarCarrito(Model model, HttpSession session) {
        logger.info("Consultando el carrito.");

        PrestamoDto carrito = servicioCarrito.getItems(session);

        if (carrito != null && carrito.getLineaPrestamoDtos() != null) {
            for (LineaPrestamoDto linea : carrito.getLineaPrestamoDtos()) {
                Material material = linea.getMaterial();
                if (material != null) {
                    logger.info("Material en el carrito - ID: {}, Nombre: {}, Cantidad: {}",
                            material.getId(), material.getNombre(), linea.getCantidadPedida());
                }
            }
        }

        model.addAttribute("carrito", carrito);
        return "mostrarcarrito";
    }

    @PostMapping("/guardarcarrito")
    public String guardarCarrito(HttpSession session, @RequestParam String usuario, @RequestParam String descripcion) {
        logger.info("Guardando carrito del usuario: {}.", usuario);

        PrestamoDto aux = servicioCarrito.getItems(session);
        aux.setUsuario(usuario);
        aux.setDescripcion(descripcion);
        servicioPrestamo.guardarDto(aux);
        servicioCarrito.removeCart(session);
        return "redirect:/";
    }
}
