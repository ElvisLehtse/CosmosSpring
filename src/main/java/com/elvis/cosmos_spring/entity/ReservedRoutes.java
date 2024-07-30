package com.elvis.cosmos_spring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservedRoutes {
    @Id
    private UUID uuid;
    private UUID reservationUuid;
    private UUID providerUuid;
}
