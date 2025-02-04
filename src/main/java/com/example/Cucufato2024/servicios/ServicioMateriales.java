package com.example.Cucufato2024.servicios;

import com.example.Cucufato2024.dto.PrestamoDto;
import com.example.Cucufato2024.entidades.Material;
import com.example.Cucufato2024.entidades.Prestamo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ServicioMateriales {
    public List<Material> findAll();
    public Material findById(long id);
    public void save(Material material);
    public void delete(Material material);
    public void importar();
    public void delete(long id);

    public Map<String, Object> obtenerDatosGrafico();
    public List<Map<String, Object>> obtenerMaterialesMasPedidos();
    public double calcularTasaAverias();
}
