package com.example.stockquotemanager.service;

import com.example.stockquotemanager.model.Stock;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuoteManagerClientService {

    List<Stock> fetchQuoteListFromManager();

}
