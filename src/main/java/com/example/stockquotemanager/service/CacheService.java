package com.example.stockquotemanager.service;

import com.example.stockquotemanager.model.Stock;
import org.springframework.stereotype.Service;

@Service
public interface CacheService {

    void reload();

    Stock get(String key);

    void clear();

    long size();
}
