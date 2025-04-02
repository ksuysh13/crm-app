package com.example.lab_project.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PriceHistoryDTO {
    private Long historyId;
    private Long productId;
    private BigDecimal price;
    private LocalDateTime changeDate;
}