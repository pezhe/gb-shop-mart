package ru.gb.gbshopmart.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gb.gbapi.common.enums.OrderStatus;
import ru.gb.gbapi.events.OrderEvent;
import ru.gb.gbapi.order.dto.OrderDto;
import ru.gb.gbshopmart.config.JmsConfig;
import ru.gb.gbshopmart.dao.AddressDao;
import ru.gb.gbshopmart.dao.CategoryDao;
import ru.gb.gbshopmart.dao.ManufacturerDao;
import ru.gb.gbshopmart.dao.OrderDao;
import ru.gb.gbshopmart.entity.Order;
import ru.gb.gbshopmart.entity.security.AccountUser;
import ru.gb.gbshopmart.security.JpaUserDetailService;
import ru.gb.gbshopmart.web.dto.mapper.OrderMapper;
import ru.gb.gbshopmart.web.model.Cart;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderDao orderDao;
    private final OrderMapper orderMapper;
    private final ManufacturerDao manufacturerDao;
    private final CategoryDao categoryDao;
    private final JmsTemplate jmsTemplate;
    private final CartService cartService;
    private final JpaUserDetailService userDetailService;
    private final AddressDao addressDao;

    public AccountUser getCurrentUserAccount() {
        String principal =
                ((UserDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()).getUsername();
        return userDetailService.findByUsername(principal);
    }

    @Transactional
    public void create(OrderDto orderDto, HttpSession session) {
        Order order = orderMapper.toOrder(orderDto, manufacturerDao, categoryDao);
        Cart cart = cartService.getCurrentCart(session);
        cart.getItems().forEach((e) -> e.setOrder(order));
        order.setAccountUser(getCurrentUserAccount());
        order.setOrderItems(cart.getItems());
        order.setDeliveryPrice(new BigDecimal("100.0"));
        order.setPrice(cart.getTotalCost().add(order.getDeliveryPrice()));
        order.setStatus(OrderStatus.CREATED);
        order.setAddress(addressDao.save(order.getAddress()));
        orderDao.save(order);
    }

    @Transactional
    public OrderDto save(final OrderDto orderDto) {
        Order order = orderMapper.toOrder(orderDto, manufacturerDao, categoryDao);
        if (order.getId() != null) {
            orderDao.findById(orderDto.getId()).ifPresent(
                    (p) -> order.setVersion(p.getVersion())
            );

        }
        OrderDto savedOrderDto = orderMapper.toOrderDto(orderDao.save(order));

        jmsTemplate.convertAndSend(JmsConfig.ORDER_CHANGED_QUEUE, new OrderEvent(savedOrderDto));

        return savedOrderDto;
    }

    @Transactional(readOnly = true)
    public OrderDto findById(Long id) {
        return orderMapper.toOrderDto(orderDao.findById(id).orElse(null));
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findByUser() {
        return orderDao
                .findAllByAccountUser(getCurrentUserAccount())
                .stream()
                .map(orderMapper::toOrderDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Order findOrderById(Long id) {
        return orderDao.findById(id).orElse(null);
    }

    public List<OrderDto> findAll() {
        return orderDao.findAll().stream().map(orderMapper::toOrderDto).collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        try {
            orderDao.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
        }
    }
}

