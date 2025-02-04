package com.example.Cucufato2024.controladores;

import com.example.Cucufato2024.entidades.Usuario;
import com.example.Cucufato2024.repositorios.RepositorioUsuarios;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class ControladorUsuarios {
    @Autowired
    private RepositorioUsuarios repositorioUsuarios;

    private static final Logger logger = LoggerFactory.getLogger(ControladorPrincipal.class);


    @GetMapping("/crud/usuarios")
    public String listaUsuarios(Model model) {
        //Solucion para poder mostrar la cantidad de usuarios en logs ya que no esta creado el servicio de Usuario
        //Obtener el Iterable de usuarios y convertirlo a una List
        Iterable<Usuario> usuariosIterable = repositorioUsuarios.findAll();
        List<Usuario> usuarios = StreamSupport.stream(usuariosIterable.spliterator(), false).toList();

        logger.info("Consultando lista de usuarios. Total de usuarios encontrados: {}", usuarios.size());

        model.addAttribute("usuarios", repositorioUsuarios.findAll());
        return "listaUsuarios";
    }

    @GetMapping("/crud/usuarios/insertar")
    public String insertarUsuario(Model model) {
        logger.info("Accediendo al formulario para insertar usuarios");

        Usuario usuario = new Usuario();
        model.addAttribute("usuario", usuario);
        return "formularioUsuarios";
    }

    @PostMapping("/crud/usuarios/insertar")
    public String insertarUsuario(@ModelAttribute Usuario usuario) {
        logger.info("Insertando nuevo usuario: {}", usuario);

        repositorioUsuarios.save(usuario);
        return "redirect:/crud/usuarios";
    }

    @GetMapping("/crud/usuarios/modificar/{id}")
    public String modificarUsuario(Model model, @PathVariable Long id) {
        logger.info("Accediendo al formulario para modificar el usuario con ID: {}", id);


        Optional<Usuario> usuario = repositorioUsuarios.findById(id);
        if (usuario.isPresent()) {
            logger.info("Estado del usuario antes de la modificación: {}", usuario.get());

            model.addAttribute("usuario", usuario.get());
        }
        return "formularioUsuarios";
    }

    @PostMapping("/crud/usuarios/modificar/{id}")
    public String actualizarUsuario(@ModelAttribute Usuario usuario, @PathVariable Long id) {
        logger.info("Usuario modificado: {}", usuario);

        usuario.setId(id);
        repositorioUsuarios.save(usuario);
        return "redirect:/crud/usuarios";
    }

    @GetMapping("/crud/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {

        Optional<Usuario> usuario = repositorioUsuarios.findById(id);
        logger.info("Eliminando usuario: {}", usuario.get());


        repositorioUsuarios.deleteById(id);

        return "redirect:/crud/usuarios";
    }
}
