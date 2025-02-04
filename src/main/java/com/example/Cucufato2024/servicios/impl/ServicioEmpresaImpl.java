package com.example.Cucufato2024.servicios.impl;

import com.example.Cucufato2024.entidades.Empresa;
import com.example.Cucufato2024.repositorios.RepositorioEmpresa;
import com.example.Cucufato2024.servicios.ServicioEmpresa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioEmpresaImpl implements ServicioEmpresa {
    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    @Override
    public List<Empresa> findAll() {
        return repositorioEmpresa.findAll();
    }

    @Override
    public Empresa findById(Long id) {
        return repositorioEmpresa.findById(id).orElse(null);
    }

    @Override
    public void save(Empresa empresa) {
        repositorioEmpresa.save(empresa);
    }

    @Override
    public void delete(Long id) {
        repositorioEmpresa.deleteById(id);
    }
}
