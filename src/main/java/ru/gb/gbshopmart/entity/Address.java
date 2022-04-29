package ru.gb.gbshopmart.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.gb.gbshopmart.entity.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "address")
@Setter
@Getter
@NoArgsConstructor
public class Address extends BaseEntity {

    @Column(name = "address")
    private String address;

    @Builder
    public Address(Long id, String address) {
        super(id);
        this.address = address;
    }
}
