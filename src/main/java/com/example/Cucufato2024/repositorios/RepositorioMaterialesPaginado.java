package com.example.Cucufato2024.repositorios;

import com.example.Cucufato2024.entidades.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioMaterialesPaginado extends PagingAndSortingRepository<Material, Long> {
    //Page<Material> findAll(Pageable pageable);
}