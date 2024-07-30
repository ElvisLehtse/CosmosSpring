package com.elvis.cosmos_spring.model;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter
public class PriceListDTO {
    private UUID id;
    private ZonedDateTime validUntil;
    private ArrayList<Leg> legs;
}
