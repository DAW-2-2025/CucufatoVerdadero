package com.example.Cucufato2024.repositorios;

import com.example.Cucufato2024.entidades.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioEmpresa extends JpaRepository<Empresa, Long> {
    public Empresa findById(long id); //Esta parte esta destinada para Multipropiedad

}
