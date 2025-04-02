package com.example.lab_project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lab_project.model.Order;
import com.example.lab_project.repository.OrderRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    //список всех задач, связанных с определённым клиентом
    public List<Order> findByClientId(Long clientId) {
        return orderRepository.findByClient_ClientId(clientId);
    }

    //Возвращает заказ по её ID  и ID клиента, если такая задача существует.
    public Optional<Order> findByOrderIdAndClientId(Long orderId, Long clientId) {
        return orderRepository.findByOrderIdAndClient_ClientId(orderId, clientId);
    }

    // //Сохраняет заказ в базе данных.
    // public Order save(Order order) {
    //     return orderRepository.save(order);
    // }

    @Transactional
    public Order save(Order order) {
        order.calculateTotalAmount(); // Пересчитываем сумму перед сохранением
        return orderRepository.save(order);
    }

    public void deleteById(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
