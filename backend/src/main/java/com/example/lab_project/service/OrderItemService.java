package com.example.lab_project.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lab_project.dto.OrderItemDTO;
import com.example.lab_project.mapper.OrderItemMapper;
import com.example.lab_project.model.Discount;
import com.example.lab_project.model.Order;
import com.example.lab_project.model.OrderItem;
import com.example.lab_project.model.Product;
import com.example.lab_project.repository.DiscountRepository;
import com.example.lab_project.repository.OrderItemRepository;
import com.example.lab_project.repository.OrderRepository;
import com.example.lab_project.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private DiscountRepository discountRepository;

    public Optional<OrderItem> findById(Long id) {
        return orderItemRepository.findById(id);
    }

    public List<OrderItem> findByOrder(Long orderId) {
        return orderItemRepository.findByOrder_OrderId(orderId);
    }

    public List<OrderItem> findByProduct(Long productId) {
        return orderItemRepository.findByProduct_ProductId(productId);
    }

    public List<OrderItem> findByDiscount(Long discountId) {
        return orderItemRepository.findByDiscount_DiscountId(discountId);
    }

    public Optional<OrderItem> findByOrderAndProduct(Long orderId, Long productId) {
        return orderItemRepository.findByOrder_OrderIdAndProduct_ProductId(orderId, productId);
    }

    //Сохраняет заказ в базе данных.
    public OrderItem save(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    public void deleteById(Long orderItemId) {
        orderItemRepository.deleteById(orderItemId);
    }

    // @Transactional
    // public OrderItemDTO create(OrderItemDTO orderItemDTO) {
    //     Order order = orderRepository.findById(orderItemDTO.getOrderId())
    //             .orElseThrow(() -> new RuntimeException("Order not found"));
    //     Product product = productRepository.findById(orderItemDTO.getProductId())
    //             .orElseThrow(() -> new RuntimeException("Product not found"));

    //     OrderItem orderItem = orderItemMapper.toEntity(orderItemDTO);
    //     orderItem.setOrder(order);
    //     orderItem.setProduct(product);

    //     if (orderItemDTO.getDiscountId() != null) {
    //         Discount discount = discountRepository.findById(orderItemDTO.getDiscountId())
    //                 .orElseThrow(() -> new RuntimeException("Discount not found"));
    //         orderItem.setDiscount(discount);
    //         orderItem.setPrice(calculateDiscountedPrice(product.getPrice(), discount));
    //     } else {
    //         orderItem.setPrice(product.getPrice());
    //     }

    //     OrderItem savedItem = orderItemRepository.save(orderItem);
    //     return orderItemMapper.toDTO(savedItem);
    // }

    // // @Transactional
    // // public Optional<OrderItemDTO> update(Long id, OrderItemDTO orderItemDTO) {
    // //     return orderItemRepository.findById(id).map(existingItem -> {
    // //         existingItem.setQuantity(orderItemDTO.getQuantity());

    // //         if (orderItemDTO.getDiscountId() != null) {
    // //             Discount discount = discountRepository.findById(orderItemDTO.getDiscountId())
    // //                     .orElseThrow(() -> new RuntimeException("Discount not found"));
    // //             existingItem.setDiscount(discount);
    // //             existingItem.setPrice(calculateDiscountedPrice(
    // //                 existingItem.getProduct().getPrice(), discount));
    // //         } else {
    // //             existingItem.setDiscount(null);
    // //             existingItem.setPrice(existingItem.getProduct().getPrice());
    // //         }

    // //         OrderItem updatedItem = orderItemRepository.save(existingItem);
    // //         return orderItemMapper.toDTO(updatedItem);
    // //     });
    // // }

    // @Transactional
    // public Optional<OrderItemDTO> update(Long id, OrderItemDTO orderItemDTO) {
    //     return orderItemRepository.findById(id)
    //         .map(existingItem -> {
    //         // Обновляем количество
    //         existingItem.setQuantity(orderItemDTO.getQuantity());
            
    //         // Загружаем продукт отдельным запросом
    //         Product product = productRepository.findById(existingItem.getProduct().getProductId())
    //             .orElseThrow(() -> new RuntimeException("Product not found"));
            
    //         // Обрабатываем скидку
    //         if (orderItemDTO.getDiscountId() != null) {
    //             Discount discount = discountRepository.findById(orderItemDTO.getDiscountId())
    //                 .orElseThrow(() -> new RuntimeException("Discount not found"));
    //             existingItem.setDiscount(discount);
    //             existingItem.setPrice(calculateDiscountedPrice(product.getPrice(), discount));
    //         } else {
    //             existingItem.setDiscount(null); // Явно устанавливаем null
    //             existingItem.setPrice(product.getPrice());
    //         }
            
    //         OrderItem updatedItem = orderItemRepository.save(existingItem);
    //         return orderItemMapper.toDTO(updatedItem);
    //     });
    // }

    // @Transactional
    // public boolean delete(Long id) {
    //     if (orderItemRepository.existsById(id)) {
    //         orderItemRepository.deleteById(id);
    //         return true;
    //     }
    //     return false;
    // }

    // private BigDecimal calculateDiscountedPrice(BigDecimal originalPrice, Discount discount) {
    //     return originalPrice.subtract(
    //         originalPrice.multiply(
    //             discount.getDiscountPercentage().divide(BigDecimal.valueOf(100))));
    // }

    @Transactional
    public OrderItemDTO create(OrderItemDTO orderItemDTO) {
        Order order = orderRepository.findById(orderItemDTO.getOrderId())
                .orElseThrow(() -> new RuntimeException("Заказ не найден"));
        
        // Проверка, что заказ не завершен
        if (order.isCompleted()) {
            throw new IllegalStateException("Нельзя добавить элемент в завершенный заказ");
        }
    
        Product product = productRepository.findById(orderItemDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Продукт не найден"));
    
        OrderItem orderItem = orderItemMapper.toEntity(orderItemDTO);
        orderItem.setOrder(order);
        orderItem.setProduct(product);
    
        if (orderItemDTO.getDiscountId() != null) {
            Discount discount = discountRepository.findById(orderItemDTO.getDiscountId())
                    .orElseThrow(() -> new RuntimeException("Скидка не найдена"));
            orderItem.setDiscount(discount);
            orderItem.setPrice(calculateDiscountedPrice(product.getPrice(), discount));
        } else {
            orderItem.setDiscount(null);
            orderItem.setPrice(product.getPrice());
        }
    
        OrderItem savedItem = orderItemRepository.save(orderItem);
        
        // Пересчитываем сумму заказа после добавления элемента
        order.calculateTotalAmount();
        orderRepository.save(order);
        
        return orderItemMapper.toDTO(savedItem);
    }

    @Transactional
    public Optional<OrderItemDTO> update(Long id, OrderItemDTO orderItemDTO) {
        return orderItemRepository.findById(id)
            .map(existingItem -> {
                Order order = existingItem.getOrder();
                
                existingItem.setQuantity(orderItemDTO.getQuantity());
                
                Product product = productRepository.findById(existingItem.getProduct().getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
                
                // if (orderItemDTO.getDiscountId() != null) {
                //     Discount discount = discountRepository.findById(orderItemDTO.getDiscountId())
                //         .orElseThrow(() -> new RuntimeException("Discount not found"));
                //     existingItem.setDiscount(discount);
                //     existingItem.setPrice(calculateDiscountedPrice(product.getPrice(), discount));
                // } else {
                //     existingItem.setDiscount(null);
                //     existingItem.setPrice(product.getPrice());
                // }

                if (orderItemDTO.getDiscountId() != null) {
                    Discount discount = discountRepository.findById(orderItemDTO.getDiscountId())
                        .orElseThrow(() -> new RuntimeException("Discount not found"));
                    existingItem.setDiscount(discount);
                    existingItem.setPrice(calculateDiscountedPrice(product.getPrice(), discount));
                } else {
                    existingItem.setDiscount(null); // Явно устанавливаем null
                    existingItem.setPrice(product.getPrice());
                }
                
                OrderItem updatedItem = orderItemRepository.save(existingItem);
                
                // Пересчитываем сумму заказа после обновления элемента
                order.calculateTotalAmount(); // Пересчет суммы
                orderRepository.save(order); // Сохраняем заказ с новой суммой
                
                return orderItemMapper.toDTO(updatedItem);
            });
    }

    @Transactional
    public boolean delete(Long id) {
        return orderItemRepository.findById(id)
            .map(orderItem -> {
                Order order = orderItem.getOrder();
                orderItemRepository.delete(orderItem);
                
                // Пересчитываем сумму заказа после удаления элемента
                order.calculateTotalAmount();
                orderRepository.save(order);
                
                return true;
            })
            .orElse(false);
    }

    private BigDecimal calculateDiscountedPrice(BigDecimal originalPrice, Discount discount) {
        // Конвертируем процент скидки (10.00%) в коэффициент (0.10)
        BigDecimal discountFactor = discount.getDiscountPercentage()
                                        .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        
        // Рассчитываем сумму скидки (99.99 * 0.10 = 9.999)
        BigDecimal discountAmount = originalPrice.multiply(discountFactor);
        
        // Рассчитываем итоговую цену (99.99 - 9.999 = 89.991) и округляем до 2 знаков
        return originalPrice.subtract(discountAmount)
                        .setScale(2, RoundingMode.HALF_UP); // → 89.99
    }
}
