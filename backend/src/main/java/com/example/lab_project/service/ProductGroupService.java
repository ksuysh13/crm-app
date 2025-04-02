package com.example.lab_project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lab_project.model.Product;
import com.example.lab_project.model.ProductGroup;
import com.example.lab_project.repository.ProductGroupRepository;
import com.example.lab_project.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductGroupService {

    @Autowired
    private ProductGroupRepository productGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<ProductGroup> findAll(String search) {
        if (search != null) {
            return productGroupRepository.findByGroupNameContainingOrDescriptionContainingIgnoreCase(search, search);
        }
        return productGroupRepository.findAll();
    }

    public Optional<ProductGroup> findById(Long id) {
        return productGroupRepository.findById(id);
    }

    public ProductGroup save(ProductGroup productGroup) {
        return productGroupRepository.save(productGroup);
    }

    public void deleteById(Long id) {
        productGroupRepository.deleteById(id);
    }

    public List<Product> getUnsoldProductsByGroup(Long groupId) {
        return productRepository.findUnsoldProductsByGroup(groupId);
    }
    
    @Transactional
    public void deleteSoldProductsByGroup(Long groupId) {
        productRepository.deleteSoldProductsByGroup(groupId);
    }
}
