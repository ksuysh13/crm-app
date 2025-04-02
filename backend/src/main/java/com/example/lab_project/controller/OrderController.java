package com.example.lab_project.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lab_project.dto.OrderDTO;
import com.example.lab_project.mapper.OrderMapper;
import com.example.lab_project.model.Order;
import com.example.lab_project.service.OrderService;

@RestController
@RequestMapping("/orders/{clientId}")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

     @GetMapping
    public List<OrderDTO> findByClientId(@PathVariable Long clientId) {
        List<Order> orders = orderService.findByClientId(clientId);
        return orders.stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> findByOrderIdAndClientId(@PathVariable Long clientId, @PathVariable Long orderId) {
        Optional<Order> order = orderService.findByOrderIdAndClientId(orderId, clientId);
        return order.map(t -> ResponseEntity.ok(orderMapper.toDTO(t)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public OrderDTO create(@PathVariable Long clientId, @RequestBody OrderDTO orderDTO) {
        orderDTO.setClientId(clientId);
        Order order = orderMapper.toEntity(orderDTO);
        Order savedOrder = orderService.save(order);
        return orderMapper.toDTO(savedOrder);
    }

    // @PutMapping("/{orderId}")
    // public ResponseEntity<OrderDTO> update(@PathVariable Long clientId, @PathVariable Long orderId, @RequestBody OrderDTO orderDTO) {
    //     if (orderService.findByOrderIdAndClientId(orderId, clientId).isEmpty()) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    //     }
    //     orderDTO.setOrderId(orderId);
    //     orderDTO.setClientId(clientId);
    //     Order order = orderMapper.toEntity(orderDTO);
    //     Order updatedOrder = orderService.save(order);
    //     return ResponseEntity.ok(orderMapper.toDTO(updatedOrder));
    // }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDTO> update(
            @PathVariable Long clientId,
            @PathVariable Long orderId,
            @RequestBody OrderDTO orderDTO) {
        
        Optional<Order> existingOrder = orderService.findByOrderIdAndClientId(orderId, clientId);
        if (existingOrder.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Order order = existingOrder.get();
        order.setOrderDate(orderDTO.getOrderDate()); // Обновляем только дату и статус
        order.setCompleted(orderDTO.isCompleted());
        
        // totalAmount не обновляем вручную — он рассчитывается автоматически!
        
        Order updatedOrder = orderService.save(order);
        return ResponseEntity.ok(orderMapper.toDTO(updatedOrder));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> delete(@PathVariable Long clientId, @PathVariable Long orderId) {
        if (orderService.findByOrderIdAndClientId(orderId, clientId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        orderService.deleteById(orderId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{orderId}/recalculate")
    public ResponseEntity<OrderDTO> recalculateOrderTotal(@PathVariable Long clientId, @PathVariable Long orderId) {
        return orderService.findByOrderIdAndClientId(orderId, clientId)
            .map(order -> {
                order.calculateTotalAmount();
                Order updatedOrder = orderService.save(order);
                return ResponseEntity.ok(orderMapper.toDTO(updatedOrder));
            })
            .orElse(ResponseEntity.notFound().build());
    }
}
