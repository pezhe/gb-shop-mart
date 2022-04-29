package ru.gb.gbshopmart.dao.security;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.gbshopmart.entity.security.AccountRole;

public interface AccountRoleDao extends JpaRepository<AccountRole, Long> {
    AccountRole findByName(String name);
}