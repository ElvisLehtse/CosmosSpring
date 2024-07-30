package com.elvis.cosmos_spring.service;

import com.elvis.cosmos_spring.entity.*;
import com.elvis.cosmos_spring.model.Leg;
import com.elvis.cosmos_spring.model.PriceListDTO;
import com.elvis.cosmos_spring.model.ProviderDTO;
import com.elvis.cosmos_spring.repository.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
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

    @Getter
    @Setter
    private static PriceList priceList;
    @Getter
    @Setter
    private static List<Planet> planetList;
    @Getter
    @Setter
    private static List<Company> companyList;
    @Getter
    @Setter
    private static List<RouteInfo> routeInfoList;
    @Getter
    @Setter
    private static List<Provider> providerList;

    private void resetLists() {
        setPlanetList(new ArrayList<>());
        setCompanyList(new ArrayList<>());
        setRouteInfoList(new ArrayList<>());
        setProviderList(new ArrayList<>());
    }

    public void initiateLists() {
        setPriceList(priceListRepository.findByValidUntilAfter(ZonedDateTime.now()).orElseThrow());
        setPlanetList(planetRepository.findByPriceList(getPriceList()));
        setCompanyList(companyRepository.findByPriceList(getPriceList()));
        setRouteInfoList(routeInfoRepository.findByPriceList(getPriceList()));
        setProviderList(providerRepository.findByPriceList(getPriceList()));
    }

    private void addPlanet(String planetName) {
        boolean isPlanetUnique = getPlanetList().stream().noneMatch(e -> e.getName().equals(planetName));
        if (isPlanetUnique) {
            Planet planet = new Planet(null, planetName, getPriceList());
            getPlanetList().add(planet);
            planetRepository.save(planet);
        }
    }
    public void writeTables(PriceListDTO priceListDTO) {
        resetLists();

        //Add priceList
        setPriceList(new PriceList(priceListDTO.getId(), priceListDTO.getValidUntil()));
        priceListRepository.save(getPriceList());

        ArrayList<Leg> legs = priceListDTO.getLegs();
        for (Leg leg : legs) {
            //Add planets
            String fromPlanetName = leg.getRouteInfo().getFrom().getName();
            String toPlanetName = leg.getRouteInfo().getTo().getName();
            addPlanet(fromPlanetName);
            addPlanet(toPlanetName);

            //Add routeInfo
            RouteInfo routeInfo = new RouteInfo(
                    leg.getRouteInfo().getId(),
                    getPriceList(),
                    getPlanetList().stream().filter(e -> e.getName().equals(fromPlanetName)).findFirst().orElseThrow(),
                    getPlanetList().stream().filter(e -> e.getName().equals(toPlanetName)).findFirst().orElseThrow(),
                    leg.getRouteInfo().getDistance()
            );
            getRouteInfoList().add(routeInfo);
            routeInfoRepository.save(routeInfo);

            //Add companies and providers
            ArrayList<ProviderDTO> providerDTOList = leg.getProviders();
            for (ProviderDTO providerDTO : providerDTOList) {
                UUID companyUuid = providerDTO.getCompany().getId();
                String companyName = providerDTO.getCompany().getName();
                boolean isCompanyUnique = getCompanyList().stream().noneMatch(e -> e.getUuid().equals(companyUuid));
                Company company = new Company(companyUuid, companyName, getPriceList());
                if (isCompanyUnique) {
                    getCompanyList().add(company);
                    companyRepository.save(company);
                }
                Provider provider = new Provider(providerDTO.getId(), company, routeInfo, providerDTO.getPrice(), providerDTO.getFlightStart(), providerDTO.getFlightEnd(), getPriceList());
                getProviderList().add(provider);
                providerRepository.save(provider);
            }
        }
    }
}
