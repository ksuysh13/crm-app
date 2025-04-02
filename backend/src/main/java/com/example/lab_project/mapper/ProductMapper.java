package com.example.lab_project.mapper;

import org.springframework.stereotype.Component;

import com.example.lab_project.dto.ProductDTO;
import com.example.lab_project.model.Manufacturer;
import com.example.lab_project.model.Product;
import com.example.lab_project.model.ProductGroup;

@Component
public class ProductMapper {
    public ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setGroupId(product.getGroup().getGroupId());
        dto.setManufacturerId(product.getManufacturer().getManufacturerId());
        // if (product.getGroup() != null) {
        //     dto.setGroupId(product.getGroup().getGroupId());
        // }
        
        // if (product.getManufacturer() != null) {
        //     dto.setManufacturerId(product.getManufacturer().getManufacturerId());
        // }
        
        // if (product.getDiscount() != null) {
        //     dto.setDiscountId(product.getDiscount().getDiscountId());
        // }
        return dto;
    }

    public Product toEntity(ProductDTO dto) {
        Product product = new Product();
        product.setProductId(dto.getProductId());
        product.setProductName(dto.getProductName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setGroup(new ProductGroup(dto.getGroupId()));
        product.setManufacturer(new Manufacturer(dto.getManufacturerId()));
        return product;
    }
}
