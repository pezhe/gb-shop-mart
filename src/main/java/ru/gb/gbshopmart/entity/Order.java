package ru.gb.gbshopmart.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.gb.gbapi.common.enums.OrderStatus;
import ru.gb.gbshopmart.entity.common.InfoEntity;
import ru.gb.gbshopmart.entity.security.AccountUser;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "shop_order")
@EntityListeners(AuditingEntityListener.class)
public class Order extends InfoEntity {

    @Column(name = "FIRSTNAME")
    private String firstname;

    @Column(name = "LASTNAME")
    private String lastname;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "MAIL")
    private String mail;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "STATUS")
    private OrderStatus status = OrderStatus.CREATED;

    @Column(name = "DELIVERY_DATE")
    private LocalDate deliveryDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AccountUser accountUser;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "order")
    List<OrderItem> orderItems;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "delivery_price")
    private BigDecimal deliveryPrice;

    @ManyToOne
    @JoinColumn(name = "delivery_address_id")
    private Address address;

    @ManyToMany
    @JoinTable(
            name = "product_order",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products;

    @Builder
    public Order(Long id, int version, String createdBy, LocalDateTime createdDate, String lastModifiedBy,
                 LocalDateTime lastModifiedDate, String firstname, String lastname, String phone, String mail,
                 OrderStatus status, LocalDate deliveryDate, AccountUser accountUser, List<OrderItem> orderItems,
                 BigDecimal price, BigDecimal deliveryPrice, Address address, Set<Product> products) {
        super(id, version, createdBy, createdDate, lastModifiedBy, lastModifiedDate);
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.mail = mail;
        this.status = status;
        this.deliveryDate = deliveryDate;
        this.accountUser = accountUser;
        this.orderItems = orderItems;
        this.price = price;
        this.deliveryPrice = deliveryPrice;
        this.address = address;
        this.products = products;
    }
}
