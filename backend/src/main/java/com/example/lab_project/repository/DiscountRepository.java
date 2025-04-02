package com.example.lab_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lab_project.model.Discount;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

}
