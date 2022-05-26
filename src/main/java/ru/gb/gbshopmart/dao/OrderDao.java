package ru.gb.gbshopmart.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.gbshopmart.entity.Order;
import ru.gb.gbshopmart.entity.security.AccountUser;

import java.util.List;

public interface OrderDao extends JpaRepository<Order, Long> {
    List<Order> findAllByAccountUser(AccountUser accountUser);
}
