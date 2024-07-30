package com.elvis.cosmos_spring.repository;

import com.elvis.cosmos_spring.entity.PriceList;
import com.elvis.cosmos_spring.entity.RouteInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RouteInfoRepository extends JpaRepository<RouteInfo, UUID> {

    List<RouteInfo> findByPriceList(PriceList priceList);
}
