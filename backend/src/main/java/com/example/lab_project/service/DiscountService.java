package com.example.lab_project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lab_project.model.Discount;
import com.example.lab_project.model.OrderItem;
import com.example.lab_project.repository.DiscountRepository;

import jakarta.transaction.Transactional;

@Service
public class DiscountService {
    @Autowired
    private DiscountRepository discountRepository;

    private OrderItemService orderItemService;

    public Optional<Discount> findById(Long id) {
        return discountRepository.findById(id);
    }

    public Discount save(Discount discount) {
        return discountRepository.save(discount);
    }

    @Transactional
    public void deleteById(Long discountId) {
        // Находим все элементы заказа с этой скидкой
        List<OrderItem> orderItems = orderItemService.findByDiscount(discountId);
        
        // Устанавливаем discount = null для каждого элемента заказа
        for (OrderItem item : orderItems) {
            item.setDiscount(null);
            item.setPrice(item.getProduct().getPrice()); // Возвращаем исходную цену
            orderItemService.save(item);
        }
        
        // Теперь можно безопасно удалить скидку
        discountRepository.deleteById(discountId);
    }

    public List<Discount> findAll() {
        return discountRepository.findAll();
    }
}
