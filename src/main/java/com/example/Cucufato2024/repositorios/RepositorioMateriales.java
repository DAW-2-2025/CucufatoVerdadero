package com.example.Cucufato2024.repositorios;

import com.example.Cucufato2024.entidades.Material;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioMateriales extends CrudRepository<Material, Long> {
    public Material findById(long id);

    // Consultas necesarias para el dashboard
    @Query("SELECT SUM(m.cantidadDisponible) FROM Material m")
    long contarDisponibles();

    @Query("SELECT SUM(m.cantidadTotal - m.cantidadDisponible) FROM Material m")
    long contarPrestados();

    @Query("SELECT SUM(m.noAveriados) FROM Material m")
    long contarNoAveriados();

    @Query("SELECT SUM(m.cantidadTotal - m.noAveriados) FROM Material m")
    long contarAveriados();

    @Query("SELECT SUM(m.cantidadTotal) FROM Material m")
    long contarTodos();
}
