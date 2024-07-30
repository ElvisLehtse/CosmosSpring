package com.elvis.cosmos_spring.controller;

import com.elvis.cosmos_spring.entity.Company;
import com.elvis.cosmos_spring.entity.Planet;
import com.elvis.cosmos_spring.entity.PriceList;
import com.elvis.cosmos_spring.entity.RouteInfo;
import com.elvis.cosmos_spring.model.PriceListDTO;
import com.elvis.cosmos_spring.repository.CompanyRepository;
import com.elvis.cosmos_spring.repository.PlanetRepository;
import com.elvis.cosmos_spring.repository.PriceListRepository;
import com.elvis.cosmos_spring.repository.RouteInfoRepository;
import com.elvis.cosmos_spring.service.BestDealCalculator;
import com.elvis.cosmos_spring.service.DatabaseWriter;
import com.elvis.cosmos_spring.util.APIReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.*;

@RestController
@CrossOrigin("http://localhost:3000")
public class CosmosController {

  @Autowired
  APIReader apiReader;
  @Autowired
  DatabaseWriter databaseWriter;
  @Autowired
  PriceListRepository priceListRepository;
  @Autowired
  PlanetRepository planetRepository;
  @Autowired
  CompanyRepository companyRepository;
  @Autowired
  RouteInfoRepository routeInfoRepository;
  @Autowired
  BestDealCalculator bestDealCalculator;
  List<Planet> planetList;
  PriceList currentPriceList;

  private void getPriceList() {
    Optional <PriceList> priceList = priceListRepository.findByValidUntilAfter(ZonedDateTime.now());
    if (priceList.isEmpty()) {
      PriceListDTO priceListDTO = apiReader.getJsonDataFromAPI();
      databaseWriter.writeTables(priceListDTO);
      priceList = Optional.of(new PriceList(priceListDTO.getId(), priceListDTO.getValidUntil()));
    }
    currentPriceList = priceList.orElseThrow();
  }

  @GetMapping("planets")
  public List<Planet> getPlanets() {
    getPriceList();
    planetList = planetRepository.findByPriceList(currentPriceList);
    return planetList;
  }

  @GetMapping("companies")
  public List<Company> getCompanies() {
    return companyRepository.findByPriceList(currentPriceList);
  }

  @GetMapping("paths")
  public List<String> getPaths(@RequestParam (value = "originPlanet") UUID originPlanet, @RequestParam (value = "destinationPlanet") UUID destinationPlanet) {
    List<String> allPaths = new ArrayList<>();
    Map<Integer, List<RouteInfo>> allPossibleRoutes = bestDealCalculator.findAllPossibleRoutes(originPlanet, destinationPlanet, new ArrayList<>(), new ArrayList<>(), new HashMap<>(), null, routeInfoRepository.findByPriceList(currentPriceList));

    for (int i = 0; i < allPossibleRoutes.size(); i++) {
      StringBuilder pathBuilder = new StringBuilder();
      for (int j = 0; j < allPossibleRoutes.get(i).size(); j++) {
        pathBuilder.append(allPossibleRoutes.get(i).get(j).getFrom_planet().getName()).append(" -> ");
        if (j == allPossibleRoutes.get(i).size() - 1) {
          pathBuilder.append(allPossibleRoutes.get(i).get(j).getTo_planet().getName());
        }
      }
      allPaths.add(pathBuilder.toString());
    }
    return allPaths;
  }

  @GetMapping("filters")
  public List<String> getFilters(@RequestParam (value = "companies") List<String> companies, @RequestParam (value = "filter") String filter) {
    List<String> allPaths = new ArrayList<>();

    return allPaths;
  }

  @GetMapping("currentPriceList")
  public boolean getCurrentPriceList() {
    Optional <PriceList> priceList = priceListRepository.findByValidUntilAfter(ZonedDateTime.now());
    return currentPriceList.getUuid().equals(priceList.orElseThrow().getUuid());
  }
}
