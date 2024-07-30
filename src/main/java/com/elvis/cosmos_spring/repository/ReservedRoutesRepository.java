package com.elvis.cosmos_spring.repository;

import com.elvis.cosmos_spring.entity.ReservedRoutes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReservedRoutesRepository extends JpaRepository<ReservedRoutes, UUID> {
}
