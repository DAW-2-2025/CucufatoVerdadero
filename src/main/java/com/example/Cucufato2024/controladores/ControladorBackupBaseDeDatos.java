package com.example.Cucufato2024.controladores;

import com.example.Cucufato2024.servicios.ServicioBackupBaseDeDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/backup")
@Controller
public class ControladorBackupBaseDeDatos {

    @Autowired
    private ServicioBackupBaseDeDatos servicioBackup;

    @GetMapping()
    public String cargarhtml(){
        return "backBaseDeDatos";
    }

    @GetMapping("/exportar")
    public String exportarBaseDeDatos(Model model) {
        try {
            servicioBackup.backup();
            model.addAttribute("errorMessage", "Se hizo la exportación");
        } catch (Exception e) {

            model.addAttribute("errorMessage", "Hubo un error: " + e.getMessage());
        }
        return "backBaseDeDatos";
    }

    @PostMapping("/importar")
    public String importarBaseDeDatos(Model model, @RequestParam("file") MultipartFile file) {
        try {
            servicioBackup.importDB(file);
            model.addAttribute("errorMessage", "Se hizo la importación");

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Hubo un error: " + e.getMessage());
        }
        return "backBaseDeDatos";
    }

    @GetMapping("/exportarZip")
    public String exportarZip(Model model) {
        try {
            servicioBackup.zipBackup();
            model.addAttribute("errorMessage", "Se hizo la exportación Zip");
        } catch (Exception e) {

            model.addAttribute("errorMessage", "Hubo un error: " + e.getMessage());
        }
        return "backBaseDeDatos";
    }
}
