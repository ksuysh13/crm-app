package com.example.lab_project.controller;

import com.example.lab_project.dto.PriceHistoryDTO;
import com.example.lab_project.service.PriceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/price-history")
public class PriceHistoryController {
    @Autowired
    private PriceHistoryService priceHistoryService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<PriceHistoryDTO>> getPriceHistoryByProductId(@PathVariable Long productId) {
        List<PriceHistoryDTO> priceHistory = priceHistoryService.getPriceHistoryByProductId(productId);
        return ResponseEntity.ok(priceHistory);
    }
}