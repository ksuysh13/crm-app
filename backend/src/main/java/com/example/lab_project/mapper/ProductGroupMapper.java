package com.example.lab_project.mapper;

import org.springframework.stereotype.Component;

import com.example.lab_project.model.ProductGroup;
import com.example.lab_project.dto.ProductGroupDTO;

@Component
public class ProductGroupMapper {
    public ProductGroupDTO toDTO(ProductGroup productGroup) {
        ProductGroupDTO dto = new ProductGroupDTO();
        dto.setGroupId(productGroup.getGroupId());
        dto.setGroupName(productGroup.getGroupName());
        dto.setDescription(productGroup.getDescription());
        return dto;
    }

    public ProductGroup toEntity(ProductGroupDTO dto){
        ProductGroup productGroup = new ProductGroup();
        productGroup.setGroupId(dto.getGroupId());
        productGroup.setGroupName(dto.getGroupName());
        productGroup.setDescription(dto.getDescription());
        return productGroup;
    }
}
