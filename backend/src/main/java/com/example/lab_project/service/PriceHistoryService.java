package com.example.lab_project.service;

import com.example.lab_project.dto.PriceHistoryDTO;
import com.example.lab_project.mapper.PriceHistoryMapper;
import com.example.lab_project.model.PriceHistory;
import com.example.lab_project.model.Product;
import com.example.lab_project.repository.PriceHistoryRepository;
import com.example.lab_project.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PriceHistoryService {
    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PriceHistoryMapper priceHistoryMapper;

    public List<PriceHistoryDTO> getPriceHistoryByProductId(Long productId) {
        List<PriceHistory> priceHistories = priceHistoryRepository.findByProduct_ProductIdOrderByChangeDateDesc(productId);
        return priceHistories.stream()
                .map(priceHistoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void recordPriceChange(Product product, BigDecimal newPrice) {
        PriceHistory priceHistory = new PriceHistory(
                product,
                newPrice,
                LocalDateTime.now()
        );
        priceHistoryRepository.save(priceHistory);
    }
}