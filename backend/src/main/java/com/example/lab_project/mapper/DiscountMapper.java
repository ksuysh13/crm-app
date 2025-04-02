package com.example.lab_project.mapper;

import org.springframework.stereotype.Component;

import com.example.lab_project.dto.DiscountDTO;
import com.example.lab_project.model.Discount;

@Component
public class DiscountMapper {
    public DiscountDTO toDTO(Discount discount){
        DiscountDTO dto = new DiscountDTO();
        dto.setDiscountId(discount.getDiscountId());
        dto.setDiscountPercentage(discount.getDiscountPercentage());
        dto.setStartDate(discount.getStartDate());
        dto.setEndDate(discount.getEndDate());
        return dto;
    }

    public Discount toEntity(DiscountDTO dto) {
        Discount discount = new Discount();
        discount.setDiscountId(dto.getDiscountId());
        discount.setDiscountPercentage(dto.getDiscountPercentage());
        discount.setStartDate(dto.getStartDate());
        discount.setEndDate(dto.getEndDate());
        return discount;
    }
}
