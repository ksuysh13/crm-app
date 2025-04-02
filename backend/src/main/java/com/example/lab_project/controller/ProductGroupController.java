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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.lab_project.dto.ProductDTO;
import com.example.lab_project.dto.ProductGroupDTO;
import com.example.lab_project.mapper.ProductGroupMapper;
import com.example.lab_project.mapper.ProductMapper;
import com.example.lab_project.model.Product;
import com.example.lab_project.model.ProductGroup;
import com.example.lab_project.service.ProductGroupService;

@RestController
@RequestMapping("/productGroup")
public class ProductGroupController {
    @Autowired
    private ProductGroupService productGroupService;

    @Autowired
    private ProductGroupMapper productGroupMapper;

    @Autowired
    private ProductMapper productMapper;

    @GetMapping
    public List<ProductGroupDTO> findAll(@RequestParam(required = false) String search) {
        List<ProductGroup> productGroups = productGroupService.findAll(search);
        return productGroups.stream()
                .map(productGroupMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{productGroupId}")
    public ResponseEntity<ProductGroupDTO> findById(@PathVariable Long productGroupId) {
        Optional<ProductGroup> productGroup = productGroupService.findById(productGroupId);
        return productGroup.map(p -> ResponseEntity.ok(productGroupMapper.toDTO(p)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ProductGroupDTO create(@RequestBody ProductGroupDTO projectDTO) {
        ProductGroup productGroup = productGroupMapper.toEntity(projectDTO);
        ProductGroup savedProductGroup = productGroupService.save(productGroup);
        return productGroupMapper.toDTO(savedProductGroup);
    }

    @PutMapping("/{productGroupId}")
    public ResponseEntity<ProductGroupDTO> update(@PathVariable Long productGroupId, @RequestBody ProductGroupDTO projectDTO) {
        if (productGroupService.findById(productGroupId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        projectDTO.setGroupId(productGroupId);
        ProductGroup productGroup = productGroupMapper.toEntity(projectDTO);
        ProductGroup updatedProductGroup = productGroupService.save(productGroup);
        return ResponseEntity.ok(productGroupMapper.toDTO(updatedProductGroup));
    }

    @DeleteMapping("/{productGroupId}")
    public ResponseEntity<Void> delete(@PathVariable Long productGroupId) {
        if (productGroupService.findById(productGroupId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        productGroupService.deleteById(productGroupId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{groupId}/unsold-products")
    public ResponseEntity<List<ProductDTO>> getUnsoldProducts(@PathVariable Long groupId) {
        if (productGroupService.findById(groupId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Product> unsoldProducts = productGroupService.getUnsoldProductsByGroup(groupId);
        List<ProductDTO> dtos = unsoldProducts.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{groupId}/sold-products")
    public ResponseEntity<Void> deleteSoldProducts(@PathVariable Long groupId) {
        if (productGroupService.findById(groupId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        productGroupService.deleteSoldProductsByGroup(groupId);
        return ResponseEntity.noContent().build();
    }
}
