package com.example.stockquotemanager.controller;


import com.example.stockquotemanager.model.HttpErrorResponse;
import com.example.stockquotemanager.model.StockQuote;
import com.example.stockquotemanager.model.dto.StockQuoteDTO;
import com.example.stockquotemanager.service.CacheService;
import com.example.stockquotemanager.service.StockQuoteService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@Log4j2
@RestController
public class StockQuoteController {

    @Autowired
    StockQuoteService stockQuoteService;

    @Autowired
    CacheService cacheService;

    @GetMapping(path = {"/get"})
    public ResponseEntity<List<StockQuote>> readAllStockQuotes() {

        log.info("Received request to retrieve all stored quotes");
        List<StockQuote> stockQuoteList = stockQuoteService.retrieveAll();

        return new ResponseEntity<>(stockQuoteList, HttpStatus.OK);
    }

    @GetMapping(path = {"/get/{id}"})
    public ResponseEntity<StockQuote> readStockQuoteById(@PathVariable("id") String id) {

        log.info("Received request to retrieve quotes for stock id: {}", id);
        StockQuote stockQuote = stockQuoteService.retrieveById(id);

        if (stockQuote == null || stockQuote.getId() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(stockQuote, HttpStatus.OK);
    }

    @PostMapping(path = {"/create"})
    public ResponseEntity createStockQuote(@RequestBody() StockQuote stockQuote) {
        StockQuoteDTO dto = new StockQuoteDTO().toDTO(stockQuote);
        log.info("Received request to register quote: {}", dto);

        HttpErrorResponse response = stockQuoteService.createQuote(dto);
        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = {"/stockcache"})
    public void clearStockCache() {
        cacheService.clear();
    }
}
