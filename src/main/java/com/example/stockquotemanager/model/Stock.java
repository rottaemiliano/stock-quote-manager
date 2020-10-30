package com.example.stockquotemanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    private String id;

    private String description;

    public Stock(Stock stock) {
        this.id = stock.getId();
        this.description = stock.getDescription();
    }

}



