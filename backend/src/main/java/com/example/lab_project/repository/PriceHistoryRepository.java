package com.example.lab_project.repository;

import com.example.lab_project.model.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
    List<PriceHistory> findByProduct_ProductIdOrderByChangeDateDesc(Long productId);
}