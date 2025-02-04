package com.example.Cucufato2024.controladores;

import com.example.Cucufato2024.entidades.Ubicacion;
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
public class ControladorUbicaciones {
    @Autowired
    RepositorioUbicacion repoUbi;
    @Autowired
    RepositorioArea repoArea;

    @GetMapping("/crud/ubicaciones")
    public String listaUbicaciones(Model model) {
        model.addAttribute("ubicacion", repoUbi.findAll());
        return "listaUbicaciones";
    }

    //Muestra el formulario para insertar nuevas categorías
    @GetMapping("/crud/ubicaciones/insertar")
    public String insertarUbicacion(Model model) {
        //Creo una categoría vacía
        Ubicacion ubi = new Ubicacion();
        //Añado la categoría vacía al modelo para "enviarla" a la vista
        model.addAttribute("ubicacion", ubi);
        model.addAttribute("areas", repoArea.findAll());
        return "formularioUbicaciones";
    }

    //Recibe los datos del formulario y añade la categoría a la base de datos
    //Esta URL debe coincidir con la de action
    //@ModelAttribute es el TH:OBJECT del formulario
    @PostMapping("/crud/ubicaciones/insertar")
    public String insertarUbicacion(@ModelAttribute Ubicacion ubicacion) {
        repoUbi.save(ubicacion);
        return "redirect:/crud/ubicaciones";
    }

    @GetMapping("/crud/ubicaciones/modificar/{id}")
    public String modificarUbicaciones(Model model, @PathVariable long id) {
        Optional<Ubicacion> ubicacion=repoUbi.findById(id);
        if(ubicacion.isPresent()) {
            model.addAttribute("ubicacion", ubicacion.get());
        }
        return "formularioUbicaciones";
    }

    @GetMapping("/crud/ubicaciones/eliminar/{id}")
    public String eliminarUbicacion(Model model, @PathVariable long id) {
        repoUbi.deleteById(id);
        return "redirect:/crud/ubicaciones";
    }

}
