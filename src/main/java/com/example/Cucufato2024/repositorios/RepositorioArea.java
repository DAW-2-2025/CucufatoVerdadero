package com.example.Cucufato2024.repositorios;

import com.example.Cucufato2024.entidades.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioArea extends JpaRepository<Area, Long> {
    public Area findByNombre(String nombre);
}

