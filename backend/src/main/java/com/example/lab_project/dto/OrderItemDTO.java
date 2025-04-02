package com.example.lab_project.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long orderItemId;
    private Long orderId;
    private Long productId;
    private Long discountId;
    private Integer quantity;
    private BigDecimal price;
}
