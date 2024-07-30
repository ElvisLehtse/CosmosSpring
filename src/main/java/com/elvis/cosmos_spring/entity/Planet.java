package com.elvis.cosmos_spring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Planet {
    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID uuid;
    @Column(length = 32)
    private String name;
    @ManyToOne
    private PriceList priceList;
}
