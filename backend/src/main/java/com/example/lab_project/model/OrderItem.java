package com.example.lab_project.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price", precision = 5, scale = 2, nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER) // Явная загрузка продукта
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @ManyToOne(fetch = FetchType.EAGER) // Явная загрузка скидки
    @JoinColumn(name = "discount_id", nullable = true) // Разрешаем NULL
    private Discount discount;

    // Методы для проверки доступности связанных сущностей
    public String getProductName() {
        return product != null ? product.getProductName() : "Недоступно";
    }

    public String getDiscountInfo() {
        return discount != null ? 
               discount.getDiscountPercentage() + "%" : "Недоступно";
    }
}
