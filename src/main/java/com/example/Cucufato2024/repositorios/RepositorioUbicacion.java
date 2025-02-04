package com.example.Cucufato2024.repositorios;

import com.example.Cucufato2024.entidades.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioUbicacion extends JpaRepository<Ubicacion, Long> {
    public Ubicacion findByNombre(String nombre);
}
