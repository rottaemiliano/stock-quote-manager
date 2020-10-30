package com.example.stockquotemanager.service.impl;

import com.example.stockquotemanager.model.HttpErrorResponse;
import com.example.stockquotemanager.model.Stock;
import com.example.stockquotemanager.model.StockQuote;
import com.example.stockquotemanager.model.dto.StockQuoteDTO;
import com.example.stockquotemanager.persistence.StockQuoteDAO;
import com.example.stockquotemanager.service.CacheService;
import com.example.stockquotemanager.service.StockQuoteService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class StockQuoteServiceImplTest {

    @Mock
    StockQuoteDAO stockQuoteDAO;

    @Mock
    CacheService cacheService;

    @InjectMocks
    StockQuoteServiceImpl stockQuoteServiceImpl;

    @Test
    public void retrieveAllTest() {
        Mockito.when(stockQuoteDAO.retrieveAll()).thenReturn(mockStockQuoteList());

        List<StockQuote> actualResponse = stockQuoteServiceImpl.retrieveAll();
        List<StockQuote> expectedResponse = mockStockQuoteList();

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void retrieveByIdTest() {
        Mockito.when(stockQuoteDAO.retrieveById("KEY_1")).thenReturn(mockStockQuoteList().get(0));

        StockQuote actualResponse = stockQuoteServiceImpl.retrieveById("KEY_1");
        StockQuote expectedResponse = mockStockQuoteList().get(0);

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void createQuoteTest() {
        Mockito.when(stockQuoteDAO.createQuote(any(StockQuoteDTO.class))).thenReturn(null);
        Mockito.when(cacheService.size()).thenReturn(5L);
        Mockito.when(cacheService.get("KEY_1")).thenReturn(new Stock("KEY_1", "Desc"));

        HttpErrorResponse actualResponse = stockQuoteServiceImpl.createQuote(new StockQuoteDTO("KEY_1", new HashMap<>()));
        HttpErrorResponse expectedResponse = null;

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void createQuoteTestCacheError() {
        Mockito.when(stockQuoteDAO.createQuote(any(StockQuoteDTO.class))).thenReturn(null);
        Mockito.when(cacheService.size()).thenReturn(0L);

        HttpErrorResponse actualResponse = stockQuoteServiceImpl.createQuote(new StockQuoteDTO());
        HttpErrorResponse expectedResponse = new HttpErrorResponse("Cache Error", "Cache service is not fully operational, cannot validate stock key");

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void createQuoteTestDatabaseError() {
        Mockito.when(stockQuoteDAO.createQuote(any(StockQuoteDTO.class))).thenReturn(new HttpErrorResponse("Database Error", "Desc"));
        Mockito.when(cacheService.size()).thenReturn(5L);
        Mockito.when(cacheService.get("KEY_1")).thenReturn(new Stock("KEY_1", "Desc"));

        HttpErrorResponse actualResponse = stockQuoteServiceImpl.createQuote(mockStock());
        HttpErrorResponse expectedResponse = new HttpErrorResponse("Database Error", "Desc");

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    private StockQuoteDTO mockStock() {
        Map<LocalDate, String> map = new HashMap<>();
        map.put(LocalDate.of(2020, 10, 15), "15");
        StockQuoteDTO stockQuoteDTO = new StockQuoteDTO("KEY_1", map);
        return stockQuoteDTO;
    }


    private List<StockQuote> mockStockQuoteList() {
        List<StockQuote> stockQuoteList = new ArrayList<>();
        Map<LocalDate, String> map = new HashMap<>();
        map.put(LocalDate.of(2020, 10, 15), "15");
        map.put(LocalDate.of(2020, 10, 16), "16");
        map.put(LocalDate.of(2020, 10, 17), "17");

        stockQuoteList.add(new StockQuote("KEY_1", map));

        return stockQuoteList;
    }
}
