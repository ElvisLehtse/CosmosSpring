package com.elvis.cosmos_spring.repository;

import com.elvis.cosmos_spring.entity.PriceList;
import org.springframework.data.jpa.repository.JpaRepository;
import com.elvis.cosmos_spring.entity.Planet;

import java.util.List;
import java.util.UUID;

public interface PlanetRepository extends JpaRepository<Planet, UUID> {

    List<Planet> findByPriceList(PriceList priceList);
}
