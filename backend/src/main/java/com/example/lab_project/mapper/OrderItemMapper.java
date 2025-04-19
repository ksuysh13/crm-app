package com.example.lab_project.mapper;

import org.springframework.stereotype.Component;

import com.example.lab_project.dto.OrderItemDTO;
import com.example.lab_project.model.Discount;
import com.example.lab_project.model.Order;
import com.example.lab_project.model.OrderItem;
import com.example.lab_project.model.Product;

@Component
public class OrderItemMapper {
    public OrderItemDTO toDTO(OrderItem orderItem) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setOrderItemId(orderItem.getOrderItemId());
        dto.setPrice(orderItem.getPrice());
        dto.setQuantity(orderItem.getQuantity());
        
        // Обработка заказа
        dto.setOrderId(orderItem.getOrder().getOrderId());
        
        // Обработка продукта
        if (orderItem.getProduct() != null) {
            dto.setProductId(orderItem.getProduct().getProductId());
            dto.setProductName(orderItem.getProduct().getProductName());
        } else {
            dto.setProductId(null);
            dto.setProductUnavailable();
        }
        
        // Обработка скидки
        if (orderItem.getDiscount() != null) {
            dto.setDiscountId(orderItem.getDiscount().getDiscountId());
            dto.setDiscountInfo(orderItem.getDiscount().getDiscountPercentage() + "%");
        } else {
            dto.setDiscountId(null);
            dto.setDiscountUnavailable();
        }
        
        return dto;
    }

    public OrderItem toEntity(OrderItemDTO dto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(dto.getOrderItemId());
        orderItem.setPrice(dto.getPrice());
        orderItem.setQuantity(dto.getQuantity());
        
        // Устанавливаем заказ
        orderItem.setOrder(new Order(dto.getOrderId()));
        
        // Устанавливаем продукт (может быть null)
        orderItem.setProduct(dto.getProductId() != null ? new Product(dto.getProductId()) : null);
        
        // Устанавливаем скидку (может быть null)
        orderItem.setDiscount(dto.getDiscountId() != null ? new Discount(dto.getDiscountId()) : null);
        
        return orderItem;
    }
}