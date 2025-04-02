package com.example.lab_project.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lab_project.model.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder_OrderId(Long orderId);
    List<OrderItem> findByProduct_ProductId(Long productId);
    List<OrderItem> findByDiscount_DiscountId(Long discountId);
    Optional<OrderItem> findByOrder_OrderIdAndProduct_ProductId(Long orderId, Long productId);
}
