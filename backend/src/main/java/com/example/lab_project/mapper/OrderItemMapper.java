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
        // dto.setDiscountId(orderItem.getDiscount().getDiscountId());
        // Обрабатываем случай, когда discount равен null
        dto.setDiscountId(orderItem.getDiscount() != null ? orderItem.getDiscount().getDiscountId() : null);
        dto.setOrderId(orderItem.getOrder().getOrderId());
        dto.setProductId(orderItem.getProduct().getProductId());
        return dto;
    }

    public OrderItem toEntity(OrderItemDTO dto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(dto.getOrderItemId());
        orderItem.setPrice(dto.getPrice());
        orderItem.setQuantity(dto.getQuantity());
        // orderItem.setDiscount(new Discount(dto.getDiscountId()));
        // Устанавливаем discount только если discountId не null
        orderItem.setDiscount(dto.getDiscountId() != null ? new Discount(dto.getDiscountId()) : null);
        orderItem.setOrder(new Order(dto.getOrderId()));
        orderItem.setProduct(new Product(dto.getProductId()));
        return orderItem;
    }
}
