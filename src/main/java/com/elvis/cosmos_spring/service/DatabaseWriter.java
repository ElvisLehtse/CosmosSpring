package com.elvis.cosmos_spring.service;

import com.elvis.cosmos_spring.entity.Company;
import com.elvis.cosmos_spring.entity.Planet;
import com.elvis.cosmos_spring.entity.PriceList;
import com.elvis.cosmos_spring.entity.RouteInfo;
import com.elvis.cosmos_spring.model.Leg;
import com.elvis.cosmos_spring.model.PriceListDTO;
import com.elvis.cosmos_spring.model.ProviderDTO;
import com.elvis.cosmos_spring.repository.CompanyRepository;
import com.elvis.cosmos_spring.repository.PlanetRepository;
import com.elvis.cosmos_spring.repository.PriceListRepository;
import com.elvis.cosmos_spring.repository.RouteInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DatabaseWriter {

    @Autowired
    PriceListRepository priceListRepository;
    @Autowired
    PlanetRepository planetRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    RouteInfoRepository routeInfoRepository;


    public void writeToDataBase(PriceListDTO priceListDTO) {
        PriceList priceList =  writePriceList(priceListDTO);
        writePlanetAndCompany(priceListDTO, priceList);
        writeRouteInfo(priceListDTO, priceList);
        writeProvider(priceListDTO);

    }
    private PriceList writePriceList(PriceListDTO priceListDTO) {
        PriceList priceList = new PriceList(priceListDTO.getId(), priceListDTO.getValidUntil());
        return priceListRepository.save(priceList);
    }

    private void writePlanetAndCompany(PriceListDTO priceListDTO, PriceList priceList) {
        ArrayList<Leg> legs = priceListDTO.getLegs();
        List <String> planetList = new ArrayList<>();
        List <UUID> companyList = new ArrayList<>();
        for (Leg leg : legs) {
            String fromPlanets = leg.getRouteInfo().getFrom().getName();
            boolean isNameUnique = !planetList.contains(fromPlanets);
            if (isNameUnique) {
                planetList.add(fromPlanets);
                Planet planet = new Planet(null, fromPlanets, priceList);
                planetRepository.save(planet);
            }
            String toPlanets = leg.getRouteInfo().getTo().getName();
            isNameUnique = !planetList.contains(toPlanets);
            if (isNameUnique) {
                planetList.add(toPlanets);
                Planet planet = new Planet(UUID.randomUUID(), toPlanets, priceList);
                planetRepository.save(planet);
            }

            ArrayList<ProviderDTO> providerList = leg.getProviders();
            for (ProviderDTO provider : providerList) {
                UUID companyUuid = provider.getCompany().getId();
                String companyName = provider.getCompany().getName();
                boolean isCompanyUnique = !companyList.contains(companyUuid);
                if (isCompanyUnique) {
                    companyList.add(companyUuid);
                    Company company = new Company(companyUuid, companyName, priceList);
                    companyRepository.save(company);
                }
            }
        }
    }

    private void writeRouteInfo(PriceListDTO priceListDTO, PriceList priceList) {
        ArrayList<Leg> legs = priceListDTO.getLegs();
        List<Planet> planetList = planetRepository.findByPriceList(priceList);
        for (Leg leg : legs) {
            UUID routeInfoUuid = leg.getRouteInfo().getId();
            String fromPlanetName = leg.getRouteInfo().getFrom().getName();
            String toPlanetName = leg.getRouteInfo().getTo().getName();
            Planet fromPlanet = planetList.stream().filter(e -> e.getName().equals(fromPlanetName)).findFirst().orElseThrow();
            Planet toPlanet = planetList.stream().filter(e -> e.getName().equals(toPlanetName)).findFirst().orElseThrow();
            Long distance = leg.getRouteInfo().getDistance();
            RouteInfo routeInfo = new RouteInfo(routeInfoUuid, priceList, fromPlanet, toPlanet, distance);
            routeInfoRepository.save(routeInfo);
        }
    }

    private void writeProvider(PriceListDTO priceListDTO) {

    }
}
