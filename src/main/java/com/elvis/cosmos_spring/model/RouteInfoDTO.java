package com.elvis.cosmos_spring.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RouteInfoDTO {
    private UUID id;
    private IdAndName from;
    private IdAndName to;
    private Long distance;
}
