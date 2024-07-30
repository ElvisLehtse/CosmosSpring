package com.elvis.cosmos_spring.service;

import com.elvis.cosmos_spring.entity.*;
import com.elvis.cosmos_spring.model.Leg;
import com.elvis.cosmos_spring.model.PriceListDTO;
import com.elvis.cosmos_spring.model.ProviderDTO;
import com.elvis.cosmos_spring.repository.*;
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
    @Autowired
    ProviderRepository providerRepository;

    private void addPlanet(List<Planet> planetList, PriceList priceList, String planetName) {
        boolean isPlanetUnique = planetList.stream().noneMatch(e -> e.getName().equals(planetName));
        if (isPlanetUnique) {
            Planet planet = new Planet(null, planetName, priceList);
            planetList.add(planet);
            planetRepository.save(planet);
        }
    }
    public void writeTables(PriceListDTO priceListDTO) {
        //Add priceList
        PriceList priceList = new PriceList(priceListDTO.getId(), priceListDTO.getValidUntil());
        priceListRepository.save(priceList);

        ArrayList<Leg> legs = priceListDTO.getLegs();
        List<Planet> planetList = new ArrayList<>();
        List <UUID> companyList = new ArrayList<>();

        for (Leg leg : legs) {
            //Add planets
            String fromPlanetName = leg.getRouteInfo().getFrom().getName();
            String toPlanetName = leg.getRouteInfo().getTo().getName();
            addPlanet(planetList, priceList, fromPlanetName);
            addPlanet(planetList, priceList, toPlanetName);

            //Add routeInfo
            RouteInfo routeInfo = new RouteInfo(
                    leg.getRouteInfo().getId(),
                    priceList,
                    planetList.stream().filter(e -> e.getName().equals(fromPlanetName)).findFirst().orElseThrow(),
                    planetList.stream().filter(e -> e.getName().equals(toPlanetName)).findFirst().orElseThrow(),
                    leg.getRouteInfo().getDistance()
            );
            routeInfoRepository.save(routeInfo);

            //Add companies and providers
            ArrayList<ProviderDTO> providerList = leg.getProviders();
            for (ProviderDTO provider : providerList) {
                UUID companyUuid = provider.getCompany().getId();
                String companyName = provider.getCompany().getName();
                boolean isCompanyUnique = !companyList.contains(companyUuid);
                Company company = new Company(companyUuid, companyName, priceList);
                if (isCompanyUnique) {
                    companyList.add(companyUuid);
                    companyRepository.save(company);
                }
                Provider providerDTO = new Provider(provider.getId(), company, routeInfo, provider.getPrice(), provider.getFlightStart(), provider.getFlightEnd());
                providerRepository.save(providerDTO);
            }
        }
    }
}
