package com.example.lab_project.mapper;

import org.springframework.stereotype.Component;

import com.example.lab_project.dto.OrderDTO;
import com.example.lab_project.model.Client;
import com.example.lab_project.model.Order;

@Component
public class OrderMapper {
    public OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCompleted(order.isCompleted());
        dto.setClientId(order.getClient().getClientId());
        return dto;
    }

    public Order toEntity(OrderDTO dto) {
        Order order = new Order();
        order.setOrderId(dto.getOrderId());
        order.setOrderDate(dto.getOrderDate());
        order.setTotalAmount(dto.getTotalAmount());
        order.setCompleted(dto.isCompleted());
        order.setClient(new Client(dto.getClientId()));
        return order;
    }
}
