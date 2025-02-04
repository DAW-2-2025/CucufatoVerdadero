package com.example.Cucufato2024.servicios.impl;

import com.example.Cucufato2024.dto.LineaPrestamoDto;
import com.example.Cucufato2024.dto.PrestamoDto;
import com.example.Cucufato2024.entidades.*;
import com.example.Cucufato2024.repositorios.RepositorioCategorias;
import com.example.Cucufato2024.repositorios.RepositorioLineaPrestamo;
import com.example.Cucufato2024.repositorios.RepositorioMateriales;
import com.example.Cucufato2024.repositorios.UserRepository;
import com.example.Cucufato2024.servicios.ServicioMateriales;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Service
public class ServicioMaterialesImpl implements ServicioMateriales {
    @Autowired
    RepositorioMateriales repositorioMateriales;
    @Autowired
    RepositorioLineaPrestamo repositorioLineaPrestamo;
    @Autowired
    RepositorioCategorias repositorioCategorias;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Material> findAll() {
        return (List<Material>) repositorioMateriales.findAll();
    }

    public Material findById(long id) {
        return repositorioMateriales.findById(id);
    }

    @Override
    public void save(Material material) {
        repositorioMateriales.save(material);
    }

    @Override
    public void delete(Material material) {
        repositorioMateriales.delete(material);
    }


    @Override
    public void importar() {
        // Obtener el usuario autenticado
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);

        if (user == null || user.getEmpresa() == null) {
            throw new RuntimeException("Usuario no autenticado o sin empresa asignada.");
        }

        // Obtener la empresa del usuario
        Empresa empresa = user.getEmpresa();

        List<String[]> todasLasLineas;
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build(); // Para que el separador sea un punto y coma
        try (CSVReader reader = new CSVReaderBuilder(new FileReader("inventario.csv")).withCSVParser(parser).withSkipLines(1).build()) {
            todasLasLineas = reader.readAll();
            // Nombre;Categoría;Departamento;CantidadTotal;CantidadDisponible;NoAveriados;Falta;Rotos;CódigoBarras;Imagen
            for (String[] linea : todasLasLineas) {
                System.out.println("linea: " + linea[0]);
                Material m = new Material();
                m.setNombre(linea[0]);
                Categoria c = repositorioCategorias.findByNombre(linea[1]);
                m.setCategoria(c);
                m.setDepartamento(linea[2]);
                m.setCantidadTotal(ServicioMaterialesImpl.convertir(linea[3]));
                m.setCantidadDisponible(ServicioMaterialesImpl.convertir(linea[3]));
                m.setNoAveriados(ServicioMaterialesImpl.convertir(linea[5]));
                m.setFalta(linea[6]);
                m.setRotos(linea[7]);
                m.setCodigoBarras(linea[8]);
                m.setImagen(linea[9]);

                // Asignar la empresa del usuario al material
                m.setEmpresa(empresa);

                repositorioMateriales.save(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        repositorioMateriales.deleteById(id);
    }



    public static int convertir(String s){
        int n=0;
        try{
            n=Integer.parseInt(s);
        }catch (NumberFormatException e){
            n=0;
        }
        return n;
    }


    public Map<String, Object> obtenerDatosGrafico() {
        long disponibles = repositorioMateriales.contarDisponibles();
        long prestados = repositorioMateriales.contarPrestados();
        long funcionales = repositorioMateriales.contarNoAveriados();
        long averiados = repositorioMateriales.contarAveriados();
        long rotos = 0;
        long total = repositorioMateriales.contarTodos();
        return Map.of(
            "total", total,
            "disponibles", disponibles,
            "prestados", prestados,
            "funcionales", funcionales,
            "averiados", averiados,
            "rotos", rotos
        );
    }

    @Override
    public List<Map<String, Object>> obtenerMaterialesMasPedidos() {
        List<Object[]> materialesMasPedidos = repositorioLineaPrestamo.findMaterialesMasPedidos();
        List<Map<String, Object>> materiales = new ArrayList<>();

        for (Object[] materialMasPedidos : materialesMasPedidos) {
            Long materialId = (Long) materialMasPedidos[0];
            BigDecimal totalPedidos = (BigDecimal) materialMasPedidos[1];

//            Double totalPedidos = totalPedidosAntes.doubleValue();

            repositorioMateriales.findById(materialId).ifPresent(material -> {
                Map<String, Object> resultado = new HashMap<>();
                resultado.put("totalPedidos", totalPedidos);
                resultado.put("id", material.getId());
                resultado.put("nombre", material.getNombre());
                resultado.put("total", material.getCantidadTotal());
                resultado.put("disponibles", material.getCantidadDisponible());
                resultado.put("codigoBarras", material.getCodigoBarras());
                resultado.put("imagen", material.getImagen());

                materiales.add(resultado);
            });
        }

        return materiales;
    }


    public double calcularTasaAverias() {
        long totalMateriales = repositorioMateriales.contarTodos();
        long averiados = repositorioMateriales.contarAveriados();
        return (totalMateriales == 0) ? 0 : (double) averiados / totalMateriales * 100;
    }
}
