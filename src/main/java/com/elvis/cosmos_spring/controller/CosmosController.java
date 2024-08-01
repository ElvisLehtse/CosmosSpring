package com.elvis.cosmos_spring.controller;

import com.elvis.cosmos_spring.entity.Company;
import com.elvis.cosmos_spring.entity.Planet;
import com.elvis.cosmos_spring.entity.PriceList;
import com.elvis.cosmos_spring.entity.RouteInfo;
import com.elvis.cosmos_spring.repository.PriceListRepository;
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
  BestDealCalculator bestDealCalculator;

  private void checkPriceListValidation() {
    if (priceListRepository.findByValidUntilAfter(ZonedDateTime.now()).isEmpty()) {
      databaseWriter.writeTables(apiReader.getJsonDataFromAPI());
    } else if (DatabaseWriter.getPriceList() == null) {
      databaseWriter.initiateLists();
    }
  }

  @GetMapping("planets")
  public List<Planet> getPlanets() {
    checkPriceListValidation();
    return DatabaseWriter.getPlanetList();
  }

  @GetMapping("companies")
  public List<Company> getCompanies() {
    checkPriceListValidation();
    return DatabaseWriter.getCompanyList();
  }

  @GetMapping("paths")
  public List<String> getPaths(@RequestParam (value = "originPlanet") UUID originPlanet, @RequestParam (value = "destinationPlanet") UUID destinationPlanet) {
    List<String> allPaths = new ArrayList<>();
    Map<Integer, List<RouteInfo>> allPossibleRoutes =
            bestDealCalculator.findAllPossibleRoutes(originPlanet, destinationPlanet, new ArrayList<>(), new ArrayList<>(), new HashMap<>(), null, DatabaseWriter.getRouteInfoList());

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
    return DatabaseWriter.getPriceList().getUuid().equals(priceList.orElseThrow().getUuid());
  }
}
