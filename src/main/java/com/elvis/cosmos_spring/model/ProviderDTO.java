package com.elvis.cosmos_spring.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class ProviderDTO {
    private UUID id;
    private IdAndName company;
    private double price;
    private Date flightStart;
    private Date flightEnd;
}
