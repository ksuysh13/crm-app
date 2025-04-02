package com.example.lab_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lab_project.model.ProductGroup;

@Repository
public interface ProductGroupRepository extends JpaRepository<ProductGroup, Long> {
    List<ProductGroup> findByGroupNameContainingOrDescriptionContainingIgnoreCase(String groupName, String description);

    List<ProductGroup> findByGroupName(String groupName);
}
