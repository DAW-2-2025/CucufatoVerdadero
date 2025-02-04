package com.example.Cucufato2024.controladores;

import com.example.Cucufato2024.entidades.Material;
import com.example.Cucufato2024.repositorios.RepositorioMaterialesPaginado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class ControladorPaginas {
    @Autowired
    private RepositorioMaterialesPaginado repo;

    @GetMapping("/materiales")
    public String listaMateriales(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size){

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<Material> paginaMateriales;
        paginaMateriales = repo.findAll(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("paginaMateriales", paginaMateriales);

        int totalPages = paginaMateriales.getTotalPages();
        if (totalPages > 0) {
            List<Integer> numerosPaginas = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("numerosPaginas", numerosPaginas);
        }

        return "listaMaterialesPaginada.html";
    }
}
