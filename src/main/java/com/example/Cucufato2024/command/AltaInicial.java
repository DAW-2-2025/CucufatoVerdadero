package com.example.Cucufato2024.command;

import com.example.Cucufato2024.entidades.Empresa;
import com.example.Cucufato2024.entidades.Role;
import com.example.Cucufato2024.entidades.User;
import com.example.Cucufato2024.repositorios.RepositorioEmpresa;
import com.example.Cucufato2024.repositorios.RoleRepository;
import com.example.Cucufato2024.repositorios.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AltaInicial implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RepositorioEmpresa empresaRepository;
    private final PasswordEncoder passwordEncoder;

    public AltaInicial(UserRepository userRepository, RoleRepository roleRepository, RepositorioEmpresa empresaRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.empresaRepository = empresaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // Crear roles
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);

            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);

            // Crear empresa
            Empresa empresa = new Empresa();
            empresa.setId(1L);
            empresa.setNombre("Cesur Audiovisual");
            empresa.setDescripcion("Departamento inicial");
            empresaRepository.save(empresa);

            // Crear usuario administrador
            User admin = new User();
            admin.setId(1L);
            admin.setName("Admin");
            admin.setEmail("admin@admin.com");
            admin.setPassword(passwordEncoder.encode("contraseña123"));
            admin.setEmpresa(empresa);
            admin.setRoles(Collections.singletonList(adminRole));
            userRepository.save(admin);
        }
    }
}