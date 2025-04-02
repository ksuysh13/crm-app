package com.example.lab_project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lab_project.model.Manufacturer;
import com.example.lab_project.model.Product;
import com.example.lab_project.repository.ManufacturerRepository;
import com.example.lab_project.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ManufacturerService {
    
    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Manufacturer> findAll(String search) {
        if (search != null) {
            return manufacturerRepository.findByManufacturerNameContainingOrCountryContainingIgnoreCase(search, search);
        }
        return manufacturerRepository.findAll();
    }

    public Optional<Manufacturer> findById(Long id) {
        return manufacturerRepository.findById(id);
    }

    public Manufacturer save(Manufacturer manufacturer) {
        return manufacturerRepository.save(manufacturer);
    }

    public void deleteById(Long id) {
        manufacturerRepository.deleteById(id);
    }

    public List<Product> getUnsoldProductsByManufacturer(Long manufacturerId) {
        return productRepository.findUnsoldProductsByManufacturer(manufacturerId);
    }
    
    @Transactional
    public void deleteSoldProductsByManufacturer(Long manufacturerId) {
        productRepository.deleteSoldProductsByManufacturer(manufacturerId);
    }
}
