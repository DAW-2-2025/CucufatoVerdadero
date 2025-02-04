package com.example.Cucufato2024.servicios.impl;

import com.example.Cucufato2024.entidades.Busqueda;
import com.example.Cucufato2024.repositorios.*;
import com.example.Cucufato2024.servicios.ServicioBusquedas;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ServicioBusquedasImpl implements ServicioBusquedas {
    @Autowired
    RepositorioBusquedas repositorioBusquedas;
    @Autowired
    RepositorioMateriales repositorioMateriales;
    @Autowired
    RepositorioLineaPrestamo repositorioLineaPrestamo;
    @Autowired
    RepositorioCategorias repositorioCategorias;
    @Autowired
    private RepositorioUsuarios repositorioUsuarios;

    @Override
    public List<Busqueda> findAll() {
        return repositorioBusquedas.findAll();
    }

    public Busqueda findById(long id) {
        return repositorioBusquedas.findById(id);
    }

    @Override
    public void save(Busqueda busqueda) {
        repositorioBusquedas.save(busqueda);
    }

    @Override
    public void delete(Busqueda busqueda) {
        repositorioBusquedas.delete(busqueda);
    }

    @Override
    public void delete(long id) {
        repositorioBusquedas.deleteById(id);
    }

    @Override
    public List<Map<String, Object>> encontrarBusquedasRecientes() {
//        Pageable busquedasConfig = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("createdAt")));

//        List<Busqueda> busquedas = repositorioBusquedas.findRecentBusquedas(busquedasConfig);
        List<Busqueda> busquedas = repositorioBusquedas.findRecentBusquedas();

        List<Map<String, Object>> resultados = new ArrayList<>();

//        List<Busqueda> busquedas = repositorioBusquedas.findAll();

//        Map<String, Busqueda> agrupadas = busquedas.stream()
//            .collect(Collectors.toMap(
//            b -> b.entidadType + b.entidadId,
//            b -> b,
//                (busqueda1, busqueda2) -> busqueda1.createdAt().isAfter(busqueda2.createdAt()) ? busqueda1 : busqueda2
//            ));

//        List<Busqueda> resultadoPrevio = new ArrayList<>(agrupadas.values());
        for (Busqueda busqueda : busquedas) {
            if ("material".equalsIgnoreCase(busqueda.getEntidadType())) {
                repositorioMateriales.findById(busqueda.getEntidadId()).ifPresent(material -> {
//                    He optado por hacer el casteo implícito del Long en vez de cambiar el tipo de la propiedad Material.id long -> Long porque podría causar conflicto con los códigos de los demás integrantes.
                    boolean exists = resultados.stream().anyMatch(resultado ->
                            "material".equals(resultado.get("tipo")) && Long.valueOf(material.getId()).equals(resultado.get("id"))
                    );

                    if (!exists) {
                        Map<String, Object> resultado = new HashMap<>();
                        resultado.put("tipo", busqueda.getEntidadType());
                        resultado.put("id", material.getId());
                        resultado.put("nombre", material.getNombre());
                        resultado.put("codigoBarras", material.getCodigoBarras());
                        resultado.put("imagen", material.getImagen());

                        resultados.add(resultado);
                    }
                });
            } else if ("usuario".equalsIgnoreCase(busqueda.getEntidadType())) {
                repositorioUsuarios.findById(busqueda.getEntidadId()).ifPresent(usuario -> {
                    boolean exists = resultados.stream().anyMatch(resultado ->
                            "usuario".equals(resultado.get("tipo")) && usuario.getId().equals(resultado.get("id"))
                    );

                    if (!exists) {
                        Map<String, Object> resultado = new HashMap<>();
                        resultado.put("tipo", busqueda.getEntidadType());
                        resultado.put("id", usuario.getId());
                        resultado.put("nombre", usuario.getNombre());
                        resultado.put("correo", usuario.getCorreo());
                        resultado.put("telefono", usuario.getTelefono());

                        resultados.add(resultado);
                    }
                });
            }
            if (resultados.size() >= 5) break;
        }

        return resultados;
    }

}
