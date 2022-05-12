package ru.gb.gbshopmart.web.model;

import lombok.Data;
import ru.gb.gbshopmart.entity.OrderItem;
import ru.gb.gbshopmart.entity.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class Cart {
    private List<OrderItem> items;
    private BigDecimal totalCost;
    private int totalCount;

    public Cart() {
        items = new ArrayList<>();
        totalCost = new BigDecimal("0.0");
    }

    public void add(Product product) {
        OrderItem orderItem = findOrderFromProduct(product);
        if (orderItem == null) {
            orderItem = OrderItem.builder()
                    .product(product)
                    .itemPrice(product.getCost())
                    .quantity(0)
                    .id(0L)
                    .totalPrice(BigDecimal.ZERO)
                    .build();
            items.add(orderItem);
        }
        orderItem.setQuantity(orderItem.getQuantity() + 1);
        recalculate();

    }

    public void setQuantity(Product product, Integer quantity) {
        OrderItem orderFromProduct = findOrderFromProduct(product);
        if (orderFromProduct == null) {
            return;
        }
        orderFromProduct.setQuantity(quantity);
        recalculate();
    }

    public void remove(Product product) {
        OrderItem orderFromProduct = findOrderFromProduct(product);
        if (orderFromProduct == null) {
            return;
        }
        items.remove(orderFromProduct);
        recalculate();
    }

    private void recalculate() {
        totalCost = BigDecimal.ZERO;
        totalCount = 0;

        for (OrderItem orderItem : items) {
            orderItem.setTotalPrice(orderItem.getProduct().getCost().multiply(new BigDecimal(orderItem.getQuantity())));
            totalCost = totalCost.add(orderItem.getTotalPrice());
            totalCount += orderItem.getQuantity();
        }
    }

    private OrderItem findOrderFromProduct(Product product) {
        return items.stream()
                .filter(orderItem -> orderItem.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);
    }
}

