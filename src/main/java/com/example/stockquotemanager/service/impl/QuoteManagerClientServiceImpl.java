package com.example.stockquotemanager.service.impl;

import com.example.stockquotemanager.model.Stock;
import com.example.stockquotemanager.service.QuoteManagerClientService;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
public class QuoteManagerClientServiceImpl implements QuoteManagerClientService {

    @Override
    public List<Stock> fetchQuoteListFromManager() {
        RestTemplate restTemplate = new RestTemplate();

        try {
            return  restTemplate.exchange(
                    "http://localhost:8080/stock",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Stock>>() {}).getBody();

        } catch (RestClientException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
