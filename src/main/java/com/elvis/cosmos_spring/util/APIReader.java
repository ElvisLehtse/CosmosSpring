package com.elvis.cosmos_spring.util;

import com.elvis.cosmos_spring.model.PriceListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class APIReader {
    @Autowired
    RestTemplate restTemplate;
    public PriceListDTO getJsonDataFromAPI() {
        String url = "https://cosmos-odyssey.azurewebsites.net/api/v1.0/TravelPrices";
        ResponseEntity<PriceListDTO> response = restTemplate.exchange(url, HttpMethod.GET, null, PriceListDTO.class);
        return response.getBody();
    }
}
