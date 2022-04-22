package ru.gb.gbexternalapi.service;

import ru.gb.gbapi.security.UserDto;
import ru.gb.gbexternalapi.entity.security.AccountUser;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserDto register(UserDto userDto);
    UserDto update(UserDto userDto);
    List<UserDto> findAll();
    Optional<AccountUser> findByUsername(String username);
    UserDto findById(Long id);
    void deleteById(Long id);
}
