package com.example.stockquotemanager.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockQuote {

    private String id;

    private Map<LocalDate, String> quotes;

    public StockQuote(StockQuote stockQuote) {
        this.id = stockQuote.getId();
        this.quotes = stockQuote.getQuotes();
    }
}



