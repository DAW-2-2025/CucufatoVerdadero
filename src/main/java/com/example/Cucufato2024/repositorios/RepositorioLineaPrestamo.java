package com.example.Cucufato2024.repositorios;

import com.example.Cucufato2024.entidades.LineaPrestamo;
import com.example.Cucufato2024.entidades.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioLineaPrestamo extends JpaRepository<LineaPrestamo, Long> {

//    @Query(value = "SELECT lp.material_id, SUM(lp.cantidad) as totalPedidos FROM linea_prestamo lp WHERE lp.fecha >= CURRENT_DATE - 30 GROUP BY lp.material_id ORDER BY totalPedidos DESC", nativeQuery = true)
    @Query(value = "SELECT lp.material_id, SUM(lp.cantidad) as totalPedidos FROM linea_prestamo lp GROUP BY lp.material_id ORDER BY totalPedidos DESC", nativeQuery = true)
    List<Object[]> findMaterialesMasPedidos();
}
