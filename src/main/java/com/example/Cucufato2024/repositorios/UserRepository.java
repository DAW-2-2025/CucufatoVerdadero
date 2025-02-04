package com.example.Cucufato2024.repositorios;

import com.example.Cucufato2024.entidades.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
