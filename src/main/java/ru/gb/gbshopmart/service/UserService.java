package ru.gb.gbshopmart.service;

import ru.gb.gbshopmart.entity.security.AccountUser;
import ru.gb.gbshopmart.web.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto register(UserDto userDto);

    UserDto update(UserDto userDto);

    UserDto findById(Long id);

    List<UserDto> findAll();

    AccountUser findByUsername(String username);

    void deleteById(Long id);
}