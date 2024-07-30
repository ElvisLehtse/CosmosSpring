package com.elvis.cosmos_spring.repository;

import com.elvis.cosmos_spring.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProviderRepository extends JpaRepository<Provider, UUID> {
}
