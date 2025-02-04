package com.example.Cucufato2024.repositorios;

import com.example.Cucufato2024.entidades.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioCategorias extends JpaRepository<Categoria, Long> {
    public Categoria findByNombre(String nombre);
}
