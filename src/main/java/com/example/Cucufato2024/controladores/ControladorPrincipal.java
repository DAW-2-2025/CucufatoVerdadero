package com.example.Cucufato2024.controladores;

import com.example.Cucufato2024.entidades.Material;
import com.example.Cucufato2024.repositorios.RepositorioArea;
import com.example.Cucufato2024.repositorios.RepositorioCategorias;
import com.example.Cucufato2024.repositorios.RepositorioMateriales;
import com.example.Cucufato2024.servicios.FileProcessingService;
import com.example.Cucufato2024.servicios.ServicioMateriales;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
public class ControladorPrincipal {

    private static final Logger logger = LoggerFactory.getLogger(ControladorPrincipal.class);

    @Autowired
    ServicioMateriales servicioMateriales;
    @Autowired
    private RepositorioCategorias repositorioCategorias;
    @Autowired
    private RepositorioArea repoArea;
    @Autowired
    private FileProcessingService fileProcessingService;
    @Autowired
    private RepositorioMateriales repositorioMateriales;

    private String ipUsuario;

    @GetMapping({"/", "/principal"})
    public String principal(HttpServletRequest request, Model model) {
        ipUsuario = obtenerIp(request);
        logger.info("Accediendo a la página principal desde la IP: {}, URL: {}", ipUsuario, request.getRequestURI());

        model.addAttribute("fecha", "18-10-2024");
        return "index";
    }

    @GetMapping({"/crud/materiales", "/crud/materiales/"})
    public String materiales(Model model) {
        logger.info("Consultando lista de materiales. Total de materiales encontrados: {}", servicioMateriales.findAll().size());

        model.addAttribute("pepito", servicioMateriales.findAll());
        return "listaMateriales";
    }

    @GetMapping("/crud/materiales/insertar")
    public String insertarMateriales(Model model) {
        logger.info("Accediendo al formulario para insertar materiales. Categorías disponibles: {}", repositorioCategorias.findAll().size());

        Material material = new Material();
        model.addAttribute("material", material);
        model.addAttribute("categorias", repositorioCategorias.findAll());
        model.addAttribute("areas", repoArea.findAll());
        return "formularioMateriales";
    }

    @PostMapping("/crud/materiales/insertar")
    public String insertarMateriales(@ModelAttribute("material") Material material, @RequestParam("fichero") MultipartFile fichero) {
        logger.info("Insertando nuevo material: {}. Archivo adjunto: {}", material, fichero.isEmpty() ? "No hay archivo adjunto" : fichero.getOriginalFilename());

        servicioMateriales.save(material);
        if (!fichero.isEmpty()) {
            String extension = fichero.getOriginalFilename();
            extension = extension.substring(extension.lastIndexOf(".") + 1);
            String img = "m-" + material.getId() + "." + extension;
            String resultadoSubida = fileProcessingService.uploadFile(fichero, img);
            material.setImagen(img);
            servicioMateriales.save(material);
            logger.info("Subiendo archivo de imagen para material con ID: {}. Nombre de archivo: {}", material.getId(), fichero.getOriginalFilename());
        }
        return "redirect:/crud/materiales/insertar";
    }

    @GetMapping("/crud/materiales/modificar/{id}")
    public String modificarMateriales(Model model, @PathVariable long id) {
        logger.info("Accediendo al formulario para modificar el material con ID: {}", id);

        Material material = repositorioMateriales.findById(id);
        logger.info("Estado del material antes de la modificación: Nombre: {}, Cantidad Disponible: {}, Cantidad Total: {}, Rotos: {}.", material.getNombre(), material.getCantidadDisponible(),material.getCantidadTotal(), material.getRotos());
        if (material == null) {
            logger.warn("Material con ID: {} no encontrado para modificar.", id);
        }
        model.addAttribute("material", material);
        model.addAttribute("areas", repoArea.findAll());
        model.addAttribute("categorias", repositorioCategorias.findAll());
        return "formularioMateriales";
    }

    @GetMapping("/crud/materiales/eliminar/{id}")
    public String eliminarMateriales(Model model, @PathVariable long id) {
        logger.info("Eliminando material con ID: {}", id);
        Material material = servicioMateriales.findById(id);

        if (servicioMateriales.findById(id) == null) {
            logger.warn("Intento de eliminar material con ID: {} fallido. No se encontró el material.", id);
        } else {
            servicioMateriales.delete(id);
            logger.info("Material eliminado - ID: {}, Nombre: {}", id, material.getNombre());
        }
        return "redirect:/crud/materiales";
    }

    @GetMapping("/crud/materiales/importar")
    public String importar2(Model model) {
        logger.info("Iniciando la importación de materiales.");

        try {
            servicioMateriales.importar();
            logger.info("Importación de materiales completada exitosamente.");
        } catch (Exception e) {
            logger.error("Error al importar los materiales: {}. Detalles del error: {}", e.getMessage(), e);
        }
        return "redirect:/crud/materiales";
    }

    private String obtenerIp(HttpServletRequest request) {
        String ipUsuario = request.getHeader("X-Forwarded-For");
        if (ipUsuario == null) {
            ipUsuario = request.getRemoteAddr();
        }
        return ipUsuario;
    }
}

