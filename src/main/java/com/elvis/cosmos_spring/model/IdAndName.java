package com.elvis.cosmos_spring.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class IdAndName {
    private UUID id;
    private String name;
}
