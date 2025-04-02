package com.example.lab_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.lab_project.model.Product;

import jakarta.transaction.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByProductNameContainingOrDescriptionContainingIgnoreCase(String productName, String description);

    // Найти все продукты по ID группы продуктов
    List<Product> findByGroup_GroupId(Long groupId);

    // Найти все продукты по ID производителя
    List<Product> findByManufacturer_ManufacturerId(Long manufacturerId);

    // Проверка существования продукта по ID
    boolean existsByProductId(Long productId);

    // Проверка существования группы по ID через продукт
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p WHERE p.group.groupId = :groupId")
    boolean existsGroupById(@Param("groupId") Long groupId);

    // Проверка существования производителя по ID через продукт
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p WHERE p.manufacturer.manufacturerId = :manufacturerId")
    boolean existsManufacturerById(@Param("manufacturerId") Long manufacturerId);

    // // Найти продукт по ID продукта и ID группы продуктов
    // Optional<Product> findByProductIdAndProductGroup_GroupId(Long productId, Long groupId);

    // // Найти продукт по ID продукта и ID производителя
    // Optional<Product> findByProductIdAndManufacturer_ManufacturerId(Long productId, Long manufacturerId);

    // // Посчитать количество не проданных продуктов группы продуктов
    // @Query("SELECT SUM(p.stockQuantity) FROM Product p WHERE p.productGroup.groupId = :groupId")
    // Integer countUnsoldProductsByGroupId(@Param("groupId") Long groupId);

    // Получить список не проданных продуктов группы продуктов (stock_quantity > 0)
    @Query(value = "SELECT p.* FROM product p WHERE p.group_id = :groupId AND p.stock_quantity > 0", nativeQuery = true)
    List<Product> findUnsoldProductsByGroup(Long groupId);

    // Получить список не проданных продуктов производителя (stock_quantity > 0)
    @Query(value = "SELECT p.* FROM product p WHERE p.manufacturer_id = :manufacturerId AND p.stock_quantity > 0", nativeQuery = true)
    List<Product> findUnsoldProductsByManufacturer(Long manufacturerId);

    // Удалить все проданные продукты по ID группы продуктов (stock_quantity = 0)
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM product WHERE group_id = :groupId AND stock_quantity = 0", nativeQuery = true)
    void deleteSoldProductsByGroup(Long groupId);

    // Удалить все проданные продукты по ID производителя (stock_quantity = 0)
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM product WHERE manufacturer_id = :manufacturerId AND stock_quantity = 0", nativeQuery = true)
    void deleteSoldProductsByManufacturer(Long manufacturerId);


    // // Удалить все проданные продукты по ID группы продуктов
    // @Modifying // Добавьте аннотацию @Modifying
    // @Query("DELETE FROM Product p WHERE p.productGroup.groupId = :groupId AND p.stockQuantity = 0")
    // int deleteSoldProductsByGroupId(@Param("groupId") Long groupId); // Возвращайте int

    // // Посчитать количество не проданных продуктов производителя
    // @Query("SELECT SUM(p.stockQuantity) FROM Product p WHERE p.manufacturer.manufacturerId = :manufacturerId")
    // Integer countUnsoldProductsByManufacturerId(@Param("manufacturerId") Long manufacturerId);

    // // Удалить все проданные продукты по ID производителя
    // @Modifying // Добавьте аннотацию @Modifying
    // @Query("DELETE FROM Product p WHERE p.manufacturer.manufacturerId = :manufacturerId AND p.stockQuantity = 0")
    // int deleteSoldProductsByManufacturerId(@Param("manufacturerId") Long manufacturerId); // Возвращайте int
}