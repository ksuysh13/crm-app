package com.example.lab_project.controller;

import java.util.List;
import java.util.Map;
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

import com.example.lab_project.dto.OrderItemDTO;
import com.example.lab_project.mapper.OrderItemMapper;
import com.example.lab_project.model.OrderItem;
import com.example.lab_project.service.OrderItemService;

@RestController
@RequestMapping("/order-items")
public class OrderItemController {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/order/{orderId}")
    public List<OrderItemDTO> findByOrder(@PathVariable Long orderId) {
        List<OrderItem> orderItems = orderItemService.findByOrder(orderId);
        return orderItems.stream()
                .map(orderItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/product/{productId}")
    public List<OrderItemDTO> findByProduct(@PathVariable Long productId) {
        List<OrderItem> orderItems = orderItemService.findByProduct(productId);
        return orderItems.stream()
                .map(orderItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/discount/{discountId}")
    public List<OrderItemDTO> findByDiscount(@PathVariable Long discountId) {
        List<OrderItem> orderItems = orderItemService.findByDiscount(discountId);
        return orderItems.stream()
                .map(orderItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<OrderItemDTO> createOrderItem(@RequestBody OrderItemDTO orderItemDTO) {
        OrderItemDTO createdItem = orderItemService.create(orderItemDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    // @PutMapping("/{id}")
    // public ResponseEntity<OrderItemDTO> updateOrderItem(
    //         @PathVariable Long id, @RequestBody OrderItemDTO orderItemDTO) {
    //     return orderItemService.update(id, orderItemDTO)
    //             .map(ResponseEntity::ok)
    //             .orElse(ResponseEntity.notFound().build());
    // }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        if (orderItemService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{orderItemId}")
    public ResponseEntity<?> updateOrderItem(
            @PathVariable Long orderItemId,
            @RequestBody OrderItemDTO orderItemDTO) {
        try {
            // log.info("Updating order item {} with data: {}", orderItemId, orderItemDTO);
            Optional<OrderItemDTO> result = orderItemService.update(orderItemId, orderItemDTO);
            return result.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            // log.error("Error updating order item: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(Map.of(
                        "error", "Failed to update order item",
                        "details", e.getMessage()
                    ));
        }
    }

}
