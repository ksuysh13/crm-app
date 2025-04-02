package com.example.lab_project.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OrderDTO {
    private Long orderId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private Long clientId;
    @JsonProperty("isCompleted")
    private boolean isCompleted;
}
