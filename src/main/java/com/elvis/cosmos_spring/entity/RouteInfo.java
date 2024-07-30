package com.elvis.cosmos_spring.entity;

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
public class RouteInfo {
    @Id
    private UUID uuid;
    @ManyToOne
    private PriceList priceList;
    @ManyToOne
    private Planet from_planet;
    @ManyToOne
    private Planet to_planet;
    private Long distance;
}
