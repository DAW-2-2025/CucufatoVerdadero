package com.example.Cucufato2024.servicios;

import com.example.Cucufato2024.entidades.Empresa;

import java.util.List;
//Este servicio esta creado para la Multipropiedad
public interface ServicioEmpresa {
    List<Empresa> findAll();
    Empresa findById(Long id);
    void save(Empresa empresa);
    void delete(Long id);
}
