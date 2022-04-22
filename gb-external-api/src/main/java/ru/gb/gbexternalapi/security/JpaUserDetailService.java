package ru.gb.gbexternalapi.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gb.gbapi.security.UserDto;
import ru.gb.gbexternalapi.dao.security.AccountRoleDao;
import ru.gb.gbexternalapi.dao.security.AccountUserDao;
import ru.gb.gbexternalapi.entity.enums.AccountStatus;
import ru.gb.gbexternalapi.entity.security.AccountRole;
import ru.gb.gbexternalapi.entity.security.AccountUser;
import ru.gb.gbexternalapi.rest.mapper.UserMapper;
import ru.gb.gbexternalapi.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JpaUserDetailService implements UserDetailsService, UserService {

    private final AccountUserDao accountUserDao;
    private final UserMapper userMapper;
    private final AccountRoleDao accountRoleDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountUser accountUser = accountUserDao.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Username: " + username + " not found")
        );
        return accountUser;
    }

    @Override
    public UserDto register(UserDto userDto) {
        if (accountUserDao.findByUsername(userDto.getUsername()).isPresent()) {
            throw new RuntimeException("User with username " + userDto.getUsername() + " already exists");
        }
        AccountUser accountUser = userMapper.toAccountUser(userDto);
        AccountRole roleUser = accountRoleDao.findByName("ROLE_USER");
        accountUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        accountUser.setRoles(Set.of(roleUser));
        accountUser.setStatus(AccountStatus.ACTIVE);

        AccountUser registeredUser = accountUserDao.save(accountUser);

        log.info("user with username {} was successfully registered", registeredUser.getUsername());

        return userMapper.toUserDto(registeredUser);
    }

    @Override
    public UserDto update(UserDto userDto) {
        return userMapper.toUserDto(accountUserDao.save(userMapper.toAccountUser(userDto)));
    }

    @Override
    public List<UserDto> findAll() {
        log.info("findAll users was called");
        return accountUserDao.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AccountUser> findByUsername(String username) {
        return accountUserDao.findByUsername(username);
    }

    @Override
    public UserDto findById(Long id) {
        return userMapper.toUserDto(accountUserDao.findById(id).orElseThrow(
        () -> new UsernameNotFoundException("Username with id: " + id + "not found")));
    }

    @Override
    public void deleteById(Long id) {
        accountUserDao.deleteById(id);
    }
}
