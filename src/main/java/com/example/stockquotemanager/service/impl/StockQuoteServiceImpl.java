package com.example.stockquotemanager.service.impl;

import com.example.stockquotemanager.model.HttpErrorResponse;
import com.example.stockquotemanager.model.StockQuote;
import com.example.stockquotemanager.model.dto.StockQuoteDTO;
import com.example.stockquotemanager.persistence.StockQuoteDAO;
import com.example.stockquotemanager.service.CacheService;
import com.example.stockquotemanager.service.StockQuoteService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
public class StockQuoteServiceImpl implements StockQuoteService {

    @Autowired
    StockQuoteDAO stockQuoteDAO;

    @Autowired
    CacheService cacheService;

    @Override
    public StockQuote retrieveById(String id) {
        return stockQuoteDAO.retrieveById(id);
    }

    @Override
    public List<StockQuote> retrieveAll() {
        return stockQuoteDAO.retrieveAll();
    }

    @Override
    public HttpErrorResponse createQuote(StockQuoteDTO stockQuoteDTO) {
        int attempt = 0;
        while (cacheService.size() < 1 && attempt < 3) {
            try {
                Thread.sleep(5000);
                cacheService.reload();
                attempt++;
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
        if (cacheService.size() < 1 ) {
            return new HttpErrorResponse("Cache Error", "Cache service is not fully operational, cannot validate stock key");
        }

        if (stockKeyIsValid(stockQuoteDTO.getId())) {
            return stockQuoteDAO.createQuote(stockQuoteDTO);
        }else {
            return new HttpErrorResponse("Validation Error", "Stock key from input is not registered in stock manager");
        }
    }

    private boolean stockKeyIsValid(String id) {
        return cacheService.get(id) != null;
    }
}
