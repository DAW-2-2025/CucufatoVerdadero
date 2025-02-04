package com.example.Cucufato2024.servicios;

import com.example.Cucufato2024.entidades.Busqueda;

import java.util.List;
import java.util.Map;

public interface ServicioBusquedas {
    public List<Busqueda> findAll();
    public Busqueda findById(long id);
    public void save(Busqueda busqueda);
    public void delete(Busqueda busqueda);
    public void delete(long id);

    List<Map<String, Object>> encontrarBusquedasRecientes();
}
