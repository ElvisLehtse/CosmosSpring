package com.elvis.cosmos_spring.model;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class ProviderDTO {
    private UUID id;
    private IdAndName company;
    private double price;
    private ZonedDateTime flightStart;
    private ZonedDateTime flightEnd;
}
