package com.example.stockquotemanager.persistence;

import com.example.stockquotemanager.model.HttpErrorResponse;
import com.example.stockquotemanager.model.StockQuote;
import com.example.stockquotemanager.model.dto.StockQuoteDTO;

import java.util.List;

public interface StockQuoteDAO {

    List<StockQuote> retrieveAll();

    StockQuote retrieveById(String id);

    HttpErrorResponse createQuote(StockQuoteDTO stockQuoteDTO);

}
