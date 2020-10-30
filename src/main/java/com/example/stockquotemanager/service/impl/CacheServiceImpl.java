package com.example.stockquotemanager.service.impl;

import com.example.stockquotemanager.model.Stock;
import com.example.stockquotemanager.service.CacheService;
import com.example.stockquotemanager.service.QuoteManagerClientService;
import com.example.stockquotemanager.service.StockQuoteService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.ref.SoftReference;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Component
public class CacheServiceImpl implements CacheService {

    public static final int TEN_MINUTES_MILLIS = 600000;

    @Autowired
    QuoteManagerClientService quoteManagerClientService;


    private final ConcurrentHashMap<String, SoftReference<Stock>> cache = new ConcurrentHashMap<>();

    public CacheServiceImpl() {
        Thread cleanerThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(TEN_MINUTES_MILLIS);
                    log.info("Cache state: {} cached quotes.", this.size());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        cleanerThread.setDaemon(true);
        cleanerThread.start();
    }

    @Override
    public void reload() {
        this.clear();

        log.info("Reloading cache.");
        List<Stock> stockList = quoteManagerClientService.fetchQuoteListFromManager();

        for (Stock stock : stockList) {
            log.info("{} added to cache.", stock.toString());
            cache.put(stock.getId(), new SoftReference<>(new Stock(stock.getId(), stock.getDescription())));
        }
    }

    @Override
    public Stock get(String id) {
        return Optional.ofNullable(cache.get(id)).map(SoftReference::get).filter(stock -> !stock.getId().isEmpty()).map(Stock::new).orElse(null);
    }

    @Override
    public void clear() {
        log.info("Cleaning cache.");
        cache.clear();
    }

    @Override
    public long size() {
        return cache.entrySet().stream().filter(quote -> Optional.ofNullable(quote.getValue()).map(SoftReference::get).map(Stock::new).isPresent()).count();
    }
}