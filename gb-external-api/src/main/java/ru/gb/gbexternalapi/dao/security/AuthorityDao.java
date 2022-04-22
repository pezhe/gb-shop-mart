package ru.gb.gbexternalapi.dao.security;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.gbexternalapi.entity.security.Authority;

public interface AuthorityDao extends JpaRepository<Authority, Long> {
}
