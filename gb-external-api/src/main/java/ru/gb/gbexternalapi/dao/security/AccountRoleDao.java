package ru.gb.gbexternalapi.dao.security;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.gbexternalapi.entity.security.AccountRole;

public interface AccountRoleDao extends JpaRepository<AccountRole, Long> {
    AccountRole findByName(String name);
}
