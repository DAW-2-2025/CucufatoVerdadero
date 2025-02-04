package com.example.Cucufato2024.repositorios;

import com.example.Cucufato2024.entidades.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
