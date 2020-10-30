package com.example.stockquotemanager.model.dto;

import com.example.stockquotemanager.model.StockQuote;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockQuoteDTO {

    private String id;

    private Map<LocalDate, String> quotes;

    public StockQuoteDTO toDTO(StockQuote stockQuote) {
        return new StockQuoteDTO(stockQuote.getId(), stockQuote.getQuotes());
    }
}
