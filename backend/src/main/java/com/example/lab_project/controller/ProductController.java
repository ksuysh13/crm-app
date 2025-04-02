package com.example.lab_project.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.lab_project.dto.ProductDTO;
import com.example.lab_project.mapper.ProductMapper;
import com.example.lab_project.model.Product;
import com.example.lab_project.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @GetMapping
    public List<ProductDTO> findAll(@RequestParam(required = false) String search) {
        List<Product> products = productService.findAll(search);
        return products.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/{groupId}/{manufacturerId}")
    public ResponseEntity<?> create(@PathVariable Long groupId, @PathVariable Long manufacturerId,
            @RequestBody ProductDTO productDTO) {
        try {
            // Проверяем существование groupId и manufacturerId перед созданием продукта
            if (!productService.existsGroupById(groupId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Group with ID " + groupId + " not found");
            }
            if (!productService.existsManufacturerById(manufacturerId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Manufacturer with ID " + manufacturerId + " not found");
            }

            productDTO.setGroupId(groupId);
            productDTO.setManufacturerId(manufacturerId);
            Product product = productMapper.toEntity(productDTO);
            Product savedProduct = productService.save(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toDTO(savedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating product: " + e.getMessage());
        }
    }

    @PutMapping("/{groupId}/{manufacturerId}/{productId}")
    public ResponseEntity<?> update(@PathVariable Long groupId, @PathVariable Long manufacturerId,
            @PathVariable Long productId, @RequestBody ProductDTO productDTO) {
        try {
            // Проверяем существование productId
            if (!productService.existsById(productId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Product with ID " + productId + " not found");
            }

            // Проверяем существование groupId и manufacturerId
            if (!productService.existsGroupById(groupId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Group with ID " + groupId + " not found");
            }
            if (!productService.existsManufacturerById(manufacturerId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Manufacturer with ID " + manufacturerId + " not found");
            }

            productDTO.setProductId(productId);
            productDTO.setGroupId(groupId);
            productDTO.setManufacturerId(manufacturerId);
            Product product = productMapper.toEntity(productDTO);
            Product updatedProduct = productService.save(product);
            return ResponseEntity.ok(productMapper.toDTO(updatedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating product: " + e.getMessage());
        }
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<?> getProductsByGroupId(@PathVariable Long groupId) {
        try {
            if (!productService.existsGroupById(groupId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Group with ID " + groupId + " not found");
            }

            List<Product> products = productService.getProductsByGroupId(groupId);
            List<ProductDTO> productDTOs = products.stream()
                    .map(productMapper::toDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(productDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving products: " + e.getMessage());
        }
    }

    @GetMapping("/manufacturer/{manufacturerId}")
    public ResponseEntity<?> getProductsByManufacturerId(@PathVariable Long manufacturerId) {
        try {
            if (!productService.existsManufacturerById(manufacturerId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Manufacturer with ID " + manufacturerId + " not found");
            }

            List<Product> products = productService.getProductsByManufacturerId(manufacturerId);
            List<ProductDTO> productDTOs = products.stream()
                    .map(productMapper::toDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(productDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving products: " + e.getMessage());
        }
    }

}
