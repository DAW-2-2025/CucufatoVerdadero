package com.example.Cucufato2024.servicios.impl;


import com.example.Cucufato2024.dto.UserDto;
import com.example.Cucufato2024.entidades.Empresa;
import com.example.Cucufato2024.entidades.Role;
import com.example.Cucufato2024.entidades.User;
import com.example.Cucufato2024.repositorios.RepositorioEmpresa;
import com.example.Cucufato2024.repositorios.RoleRepository;
import com.example.Cucufato2024.repositorios.UserRepository;
import com.example.Cucufato2024.servicios.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    private RepositorioEmpresa empresaRepository;

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Asocia el rol al usuario
        Role role = roleRepository.findByName("ROLE_USER");
        user.setRoles(Arrays.asList(role));

        // Asocia la empresa al usuario
        if (userDto.getEmpresaId() != null) {
            //Esta parte esta destinada para Multipropiedad
            Empresa empresa = empresaRepository.findById(userDto.getEmpresaId())
                    .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
            user.setEmpresa(empresa);
        }

        userRepository.save(user);
    }


    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> convertEntityToDto(user))
                .collect(Collectors.toList());
    }

    private UserDto convertEntityToDto(User user){
        UserDto userDto = new UserDto();
        String[] name = user.getName().split(" ");
        userDto.setFirstName(name[0]);
        userDto.setLastName(name[1]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }



    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }
}
