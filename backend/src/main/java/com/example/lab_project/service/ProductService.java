package com.example.lab_project.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lab_project.model.OrderItem;
import com.example.lab_project.model.Product;
import com.example.lab_project.repository.ManufacturerRepository;
import com.example.lab_project.repository.ProductGroupRepository;
import com.example.lab_project.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductGroupRepository productGroupRepository;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private OrderItemService orderItemService;

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        // Проверка на отрицательную цену
        if (product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Цена продукта не может быть отрицательной");
        }
        
        // Проверка на отрицательное количество
        if (product.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Количество товара не может быть отрицательным");
        }
        
        return productRepository.save(product);
    }

    public List<Product> findAll(String search) {
        if (search != null) {
            return productRepository.findByProductNameContainingOrDescriptionContainingIgnoreCase(search, search);
        }
        return productRepository.findAll();
    }

    // Найти все продукты по ID группы продуктов
    public List<Product> getProductsByGroupId(Long groupId) {
        return productRepository.findByGroup_GroupId(groupId);
    }

    // Найти все продукты по ID производителя
    public List<Product> getProductsByManufacturerId(Long manufacturerId) {
        return productRepository.findByManufacturer_ManufacturerId(manufacturerId);
    }

    // Проверка существования продукта по ID
    public boolean existsById(Long productId) {
        return productRepository.existsByProductId(productId);
    }

    // Проверка существования группы по ID
    public boolean existsGroupById(Long groupId) {
        return productGroupRepository.existsById(groupId);
    }

    // Проверка существования производителя по ID
    public boolean existsManufacturerById(Long manufacturerId) {
        return manufacturerRepository.existsById(manufacturerId);
    }

    @Transactional
    public void deleteById(Long productId) {
        // Находим все элементы заказа с этим товаром
        List<OrderItem> orderItems = orderItemService.findByProduct(productId);
        
        // Устанавливаем product = null для каждого элемента заказа
        for (OrderItem item : orderItems) {
            item.setProduct(null);
            orderItemService.save(item);
        }
        
        // Теперь можно безопасно удалить товар
        productRepository.deleteById(productId);
    }

    // // Найти продукт по ID продукта и ID группы продуктов
    // public Optional<Product> getProductByIdAndGroupId(Long productId, Long groupId) {
    //     return productRepository.findByProductIdAndProductGroup_GroupId(productId, groupId);
    // }

    // // Найти продукт по ID продукта и ID производителя
    // public Optional<Product> getProductByIdAndManufacturerId(Long productId, Long manufacturerId) {
    //     return productRepository.findByProductIdAndManufacturer_ManufacturerId(productId, manufacturerId);
    // }

    // // Посчитать количество не проданных продуктов группы продуктов
    // public Integer countUnsoldProductsByGroupId(Long groupId) {
    //     return productRepository.countUnsoldProductsByGroupId(groupId);
    // }

    // // Посчитать количество не проданных продуктов производителя
    // public Integer countUnsoldProductsByManufacturerId(Long manufacturerId) {
    //     return productRepository.countUnsoldProductsByManufacturerId(manufacturerId);
    // }

    // // Удалить все проданные продукты по ID группы продуктов
    // public int deleteSoldProductsByGroupId(Long groupId) {
    //     return productRepository.deleteSoldProductsByGroupId(groupId);
    // }

    // // Удалить все проданные продукты по ID производителя
    // public int deleteSoldProductsByManufacturerId(Long manufacturerId) {
    //     return productRepository.deleteSoldProductsByManufacturerId(manufacturerId);
    // }
}
