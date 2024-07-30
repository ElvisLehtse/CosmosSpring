package com.elvis.cosmos_spring.repository;

import com.elvis.cosmos_spring.entity.Company;
import com.elvis.cosmos_spring.entity.PriceList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

    List<Company> findByPriceList(PriceList priceList);
}
