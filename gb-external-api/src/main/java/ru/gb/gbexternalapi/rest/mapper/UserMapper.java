package ru.gb.gbexternalapi.rest.mapper;

import org.mapstruct.Mapper;
import ru.gb.gbapi.security.UserDto;
import ru.gb.gbexternalapi.entity.security.AccountUser;

@Mapper
public interface UserMapper {

    AccountUser toAccountUser(UserDto userDto);
    UserDto toUserDto(AccountUser accountUser);
}
