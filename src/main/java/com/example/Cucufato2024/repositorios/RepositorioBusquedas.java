package com.example.Cucufato2024.repositorios;

import com.example.Cucufato2024.entidades.Busqueda;
import com.example.Cucufato2024.entidades.Material;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioBusquedas extends JpaRepository<Busqueda, Long> {
    public Busqueda findById(long id);

    @Query("SELECT b FROM Busqueda b ORDER BY b.createdAt DESC")
//    List<Busqueda> findRecentBusquedas(Pageable pageable);
    List<Busqueda> findRecentBusquedas();
}
