package com.example.lab_project.mapper;

import org.springframework.stereotype.Component;

import com.example.lab_project.dto.PriceHistoryDTO;
import com.example.lab_project.model.PriceHistory;
import com.example.lab_project.model.Product;

@Component
public class PriceHistoryMapper {
    public PriceHistoryDTO toDTO(PriceHistory priceHistory) {
        PriceHistoryDTO dto = new PriceHistoryDTO();
        dto.setHistoryId(priceHistory.getHistoryId());
        dto.setProductId(priceHistory.getProduct().getProductId());
        dto.setPrice(priceHistory.getPrice());
        dto.setChangeDate(priceHistory.getChangeDate());
        return dto;
    }

    public PriceHistory toEntity(PriceHistoryDTO dto) {
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setHistoryId(dto.getHistoryId());
        priceHistory.setProduct(new Product(dto.getProductId()));
        priceHistory.setPrice(dto.getPrice());
        priceHistory.setChangeDate(dto.getChangeDate());
        return priceHistory;
    }
}