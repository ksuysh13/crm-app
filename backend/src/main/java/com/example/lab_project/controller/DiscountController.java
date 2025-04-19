package com.example.lab_project.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lab_project.dto.DiscountDTO;
import com.example.lab_project.mapper.DiscountMapper;
import com.example.lab_project.model.Discount;
import com.example.lab_project.service.DiscountService;

@RestController
@RequestMapping("/discounts")
public class DiscountController {
    @Autowired
    private DiscountService discountService;

    @Autowired
    private DiscountMapper discountMapper;

    @GetMapping
    public List<DiscountDTO> findAll() {
        List<Discount> discounts = discountService.findAll();
        return discounts.stream()
                .map(discountMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{discountId}")
    public ResponseEntity<DiscountDTO> findById(@PathVariable Long discountId) {
        Optional<Discount> discount = discountService.findById(discountId);
        return discount.map(p -> ResponseEntity.ok(discountMapper.toDTO(p)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public DiscountDTO create(@RequestBody DiscountDTO discountDTO) {
        Discount discount = discountMapper.toEntity(discountDTO);
        Discount savedDiscount = discountService.save(discount);
        return discountMapper.toDTO(savedDiscount);
    }

    @PutMapping("/{discountId}")
    public ResponseEntity<DiscountDTO> update(@PathVariable Long discountId, @RequestBody DiscountDTO discountDTO) {
        if (discountService.findById(discountId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        discountDTO.setDiscountId(discountId);
        Discount discount = discountMapper.toEntity(discountDTO);
        Discount updatedDiscount = discountService.save(discount);
        return ResponseEntity.ok(discountMapper.toDTO(updatedDiscount));
    }

    @DeleteMapping("/{discountId}")
    public ResponseEntity<Void> delete(@PathVariable Long discountId) {
        if (discountService.findById(discountId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        discountService.deleteById(discountId);
        return ResponseEntity.noContent().build();
    }
}
