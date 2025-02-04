package com.example.Cucufato2024.controladores;

import com.example.Cucufato2024.entidades.Area;
import com.example.Cucufato2024.repositorios.RepositorioArea;
import com.example.Cucufato2024.repositorios.RepositorioUbicacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class ControladorAreas {
    @Autowired
    RepositorioArea repoArea;
    @Autowired
    RepositorioUbicacion repoUbi;

    @GetMapping("/crud/area")
    public String listarArea(Model model) {
        model.addAttribute("areas", repoArea.findAll());
        return "listaAreas";
    }

    @GetMapping("/crud/area/insertar")
    public String insertarArea(Model model) {
        Area area = new Area();

        model.addAttribute("area", area);
        model.addAttribute("ubicaciones", repoUbi.findAll());
        return "formularioAreas";
    }

    @PostMapping("/crud/area/insertar")
    public String insertarArea(@ModelAttribute Area area) {
        repoArea.save(area);
        return "redirect:/crud/area";
    }

    @GetMapping("/crud/area/modificar/{id}")
    public String modificarArea(Model model, @PathVariable long id) {
        Optional<Area> area=repoArea.findById(id);
        if(area.isPresent()) {
            model.addAttribute("area", area.get());
        }
        model.addAttribute("ubicaciones", repoUbi.findAll());
        return "formularioAreas";
    }

    @GetMapping("/crud/area/eliminar/{id}")
    public String eliminarArea(Model model, @PathVariable long id) {
        repoArea.deleteById(id);
        return "redirect:/crud/area";
    }

}
