package com.example.lab_project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.lab_project.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByClient_ClientId(Long clientId);

    Optional<Order> findByOrderIdAndClient_ClientId(Long orderId, Long clientId); 

    @Query(value = "SELECT o.* FROM ord o WHERE o.client_id = :clientId AND o.is_completed = false", nativeQuery = true)
    List<Order> findUncompletedOrdersByClient(Long clientId);
}
