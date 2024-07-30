package com.elvis.cosmos_spring.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter
public class Leg {
    private UUID id;
    private RouteInfoDTO routeInfo;
    private ArrayList<ProviderDTO> providers;
}
