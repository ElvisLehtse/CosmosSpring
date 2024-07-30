package com.elvis.cosmos_spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    @Id
    private UUID uuid;
    @Column(length = 64)
    private String name;
    @ManyToOne
    private PriceList priceList;
}
