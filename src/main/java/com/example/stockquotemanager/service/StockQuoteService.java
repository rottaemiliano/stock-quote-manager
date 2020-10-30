package com.example.stockquotemanager.service;

import com.example.stockquotemanager.model.HttpErrorResponse;
import com.example.stockquotemanager.model.StockQuote;
import com.example.stockquotemanager.model.dto.StockQuoteDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StockQuoteService {

    StockQuote retrieveById(String id);

    List<StockQuote> retrieveAll();

    HttpErrorResponse createQuote(StockQuoteDTO stockQuoteDTO);

}
