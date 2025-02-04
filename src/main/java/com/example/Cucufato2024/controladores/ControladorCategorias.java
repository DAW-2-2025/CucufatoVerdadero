package com.example.Cucufato2024.controladores;

import com.example.Cucufato2024.entidades.Categoria;
import com.example.Cucufato2024.repositorios.RepositorioCategorias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@Controller
public class ControladorCategorias {
    @Autowired
    RepositorioCategorias repositorioCategorias;
    private static final Logger logger = LoggerFactory.getLogger(ControladorPrincipal.class);

    @GetMapping("/crud/categorias")
    public String listaCategorias(Model model) {
        logger.info("Consultando lista de categorías. Total de categorías encontradas: {}", repositorioCategorias.findAll().size());
        model.addAttribute("maruja", repositorioCategorias.findAll());
        return "listaCategorias";
    }

    @GetMapping("/crud/categorias/insertar")
    public String insertarCategorias(Model model) {
        logger.info("Accediendo al formulario para insertar categorías");
        // Creo una categoría vacía
        Categoria categoria = new Categoria();
        // Añado la categoría vacía al modelo para "enviarla" a la vista
        model.addAttribute("categoria", categoria);
        return "formularioCategorias";
    }

    @PostMapping("/crud/categorias/insertar")
    public String insertarCategorias(@ModelAttribute Categoria categoria, RedirectAttributes redirectAttributes) {
        logger.info("Insertando nueva categoría: {}", categoria);
        repositorioCategorias.save(categoria);
        redirectAttributes.addFlashAttribute("message", "Categoría guardada correctamente.");
        return "redirect:/crud/categorias";
    }

    @GetMapping("/crud/categorias/modificar/{id}")
    public String modificarCategorias(Model model, @PathVariable long id) {
        logger.info("Accediendo al formulario para modificar la categoría con ID: {}", id);
        Optional<Categoria> categoria = repositorioCategorias.findById(id);
        if (categoria.isPresent()) {
            logger.info("Estado de la categoría antes de la modificación: {}", categoria.get());
            model.addAttribute("categoria", categoria.get());
        }
        return "formularioCategorias";
    }

    @GetMapping("/crud/categorias/eliminar/{id}")
    public String eliminarCategoria(@PathVariable long id, RedirectAttributes redirectAttributes) {
        repositorioCategorias.deleteById(id);
        redirectAttributes.addFlashAttribute("elimMessage", "Categoría eliminada correctamente.");
        return "redirect:/crud/categorias";
    }
}
