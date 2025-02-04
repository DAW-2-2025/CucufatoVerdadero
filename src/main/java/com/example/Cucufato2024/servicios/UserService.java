package com.example.Cucufato2024.servicios;



import com.example.Cucufato2024.dto.UserDto;
import com.example.Cucufato2024.entidades.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    List<UserDto> findAllUsers();
}
