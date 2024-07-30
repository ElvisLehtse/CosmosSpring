package com.elvis.cosmos_spring.service;

import com.elvis.cosmos_spring.entity.RouteInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BestDealCalculator {

    public List<RouteInfo> routeList;

    public Map<Integer, List<RouteInfo>> findAllPossibleRoutes(UUID originPlanet, UUID destinationPlanet, List<UUID> currentPath,
                                      List<RouteInfo> listOfRoutes, Map<Integer, List<RouteInfo>> allPossibleRoutes, RouteInfo routeInfo) {
        currentPath.add(originPlanet);
        if (routeInfo != null) {
            listOfRoutes.add(routeInfo);
        }
        if (originPlanet.equals(destinationPlanet)) {
            allPossibleRoutes.put(allPossibleRoutes.size(), listOfRoutes);
        }
        for (RouteInfo route : routeList) {
            if (route.getFrom_planet().getUuid().equals(originPlanet) && !currentPath.contains(route.getTo_planet().getUuid())) {
                findAllPossibleRoutes(route.getTo_planet().getUuid(), destinationPlanet, new ArrayList<>(currentPath),
                            new ArrayList<>(listOfRoutes), allPossibleRoutes, route);
            }
        }
        return allPossibleRoutes;
    }
}
