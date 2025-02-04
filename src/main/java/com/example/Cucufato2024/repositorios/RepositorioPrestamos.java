package com.example.Cucufato2024.repositorios;

import com.example.Cucufato2024.entidades.Material;
import com.example.Cucufato2024.entidades.Prestamo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface RepositorioPrestamos extends JpaRepository<Prestamo, Long> {
    
    // Consultas necesarias para el dashboard
    List<Prestamo> findByActivoTrue();

    @Query("SELECT COUNT(p) FROM Prestamo p WHERE p.fechaDevolucion < CURRENT_DATE AND p.activo = true")
    long contarPorMorosidad();

    @Query("SELECT COUNT(p) FROM Prestamo p WHERE p.activo = false")
    long contarResueltos();

    @Query("SELECT COUNT(p) FROM Prestamo p")
    long contarTodos();

}
