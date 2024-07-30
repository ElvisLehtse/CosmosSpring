package com.elvis.cosmos_spring.repository;

import com.elvis.cosmos_spring.entity.PriceList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public interface PriceListRepository extends JpaRepository<PriceList, UUID> {

    Optional<PriceList> findByValidUntilAfter(ZonedDateTime validUntil);
}
