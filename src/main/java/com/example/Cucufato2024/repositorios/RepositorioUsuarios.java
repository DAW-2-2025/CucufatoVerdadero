package com.example.Cucufato2024.repositorios;

import com.example.Cucufato2024.entidades.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioUsuarios extends CrudRepository<Usuario, Long> {
}
