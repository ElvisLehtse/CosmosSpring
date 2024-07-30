package com.elvis.cosmos_spring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Provider {
    @Id
    private UUID uuid;
    @ManyToOne
    private Company company;
    @ManyToOne
    private RouteInfo route_info;
    private Long price;
    private ZonedDateTime flight_start;
    private ZonedDateTime flight_end;

}
