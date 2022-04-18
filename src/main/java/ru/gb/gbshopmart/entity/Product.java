package ru.gb.gbshopmart.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.gb.gbshopmart.entity.common.BaseEntity;
import ru.gb.gbshopmart.entity.common.InfoEntity;
import ru.gb.gbshopmart.entity.enums.Status;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table (name = "product")
@EntityListeners(AuditingEntityListener.class)
public class Product extends InfoEntity {

    @Column(name = "title")
    private String title;
    @Column(name = "cost")
    private BigDecimal cost;
    @Column(name = "manufacture_date")
    private LocalDate manufactureDate;
    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    // в базу данных в таблицу product колонка category_id пока не добавлена
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", cost=" + cost +
                ", manufactureDate=" + manufactureDate +
//                ", manufacturer=" + manufacturer.getName() +
                "}\n";
    }

    @Builder
    public Product(Long id, int version, String createdBy, LocalDateTime createdDate, String lastModifiedBy,
                   LocalDateTime lastModifiedDate, String title, BigDecimal cost, LocalDate manufactureDate,
                   Manufacturer manufacturer, Status status) {
        super(id, version, createdBy, createdDate, lastModifiedBy, lastModifiedDate);
        this.title = title;
        this.cost = cost;
        this.manufactureDate = manufactureDate;
        this.manufacturer = manufacturer;
        this.status = status;
    }
}
