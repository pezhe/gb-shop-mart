package ru.gb.gbshopmart.web.dto.mapper;

import org.mapstruct.Mapper;
import ru.gb.gbshopmart.entity.security.AccountUser;
import ru.gb.gbshopmart.web.dto.UserDto;

@Mapper
public interface UserMapper {
    AccountUser toAccountUser(UserDto userDto);
    UserDto toUserDto(AccountUser accountUser);
}