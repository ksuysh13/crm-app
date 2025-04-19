package com.example.lab_project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lab_project.model.Discount;
import com.example.lab_project.repository.DiscountRepository;

@Service
public class DiscountService {
    @Autowired
    private DiscountRepository discountRepository;

    public Optional<Discount> findById(Long id) {
        return discountRepository.findById(id);
    }

    public Discount save(Discount discount) {
        return discountRepository.save(discount);
    }

    public void deleteById(Long id) {
        discountRepository.deleteById(id);
    }

    public List<Discount> findAll() {
        return discountRepository.findAll();
    }
}
