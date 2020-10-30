package com.example.stockquotemanager.persistence.impl;

import com.example.stockquotemanager.model.HttpErrorResponse;
import com.example.stockquotemanager.model.StockQuote;
import com.example.stockquotemanager.model.dto.StockQuoteDTO;
import com.example.stockquotemanager.persistence.StockQuoteDAO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Component
public class StockQuoteDAOImpl implements StockQuoteDAO {

    @Autowired
    private DataSource dataSource;

    @Override
    public List<StockQuote> retrieveAll() {
        List<StockQuote> stockQuoteList = new ArrayList<>();
        try {
            PreparedStatement ps = dataSource.getConnection().prepareStatement(
                    "SELECT quote_key, " +
                            "quote_date, " +
                            "quote_value " +
                            "FROM quote;");
            ResultSet rs = ps.executeQuery();
            Map<String, Map<LocalDate, String>> stockQuoteMap = buildStockQuoteHashmaps(rs);
            if (stockQuoteMap.keySet().size() > 0) {
                for (String key : stockQuoteMap.keySet()) {
                    stockQuoteList.add(new StockQuote(key, stockQuoteMap.get(key)));
                }
            }
            dataSource.getConnection().close();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return stockQuoteList;
    }

    @Override
    public StockQuote retrieveById(String id) {
        StockQuote stockQuote = null;
        try {
            PreparedStatement ps = dataSource.getConnection().prepareStatement(
                    "SELECT quote_key, " +
                            "quote_date, " +
                            "quote_value " +
                            "FROM quote WHERE quote_key = ?;");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            Map<String, Map<LocalDate, String>> stockQuoteMap = buildStockQuoteHashmaps(rs);
            if (stockQuoteMap.keySet().size() > 0) {
                stockQuote = new StockQuote(id, stockQuoteMap.get(id));
            }
            dataSource.getConnection().close();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return stockQuote;
    }

    @Override
    public HttpErrorResponse createQuote(StockQuoteDTO stockQuoteDTO) {
        try {
            PreparedStatement ps = dataSource.getConnection().prepareStatement(
                    "INSERT INTO quote " +
                            "VALUES (NULL, ?, ?, ?);");
            for (LocalDate quoteKey : stockQuoteDTO.getQuotes().keySet()) {
                ps.setString(1, stockQuoteDTO.getId());
                ps.setDate(2, Date.valueOf(quoteKey));
                ps.setString(3, stockQuoteDTO.getQuotes().get(quoteKey));
                ps.addBatch();
            }
            ps.executeBatch();
            dataSource.getConnection().close();

            return null;
        } catch (SQLException e) {
            log.error(e.getMessage());
            return new HttpErrorResponse("Database Error", e.getLocalizedMessage());
        }
    }

    private Map<String, Map<LocalDate, String>> buildStockQuoteHashmaps(ResultSet rs) throws SQLException {
        Map<String, Map<LocalDate, String>> quoteMap = new HashMap<>();
        while (rs.next()) {
            String key = rs.getString("quote_key");
            LocalDate date = rs.getDate("quote_date").toLocalDate();
            String value = rs.getString("quote_value");

            if (quoteMap.containsKey(key)) {
                Map<LocalDate, String> innerMap = quoteMap.get(key);
                if (innerMap.containsKey(date)) {
                    innerMap.remove(date);
                }
                innerMap.put(date, value);
            } else {
                Map<LocalDate, String> valuesMap = new HashMap<>();
                valuesMap.put(date, value);
                quoteMap.put(key, valuesMap);
            }
        }
        return quoteMap;
    }
}
