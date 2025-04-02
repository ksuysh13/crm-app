package com.example.lab_project.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class DiscountDTO {
    private Long discountId;
    private BigDecimal discountPercentage;
    private LocalDate startDate;
    private LocalDate endDate;
}
