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

import com.example.lab_project.dto.ManufacturerDTO;
import com.example.lab_project.dto.ProductDTO;
import com.example.lab_project.mapper.ManufacturerMapper;
import com.example.lab_project.mapper.ProductMapper;
import com.example.lab_project.model.Manufacturer;
import com.example.lab_project.model.Product;
import com.example.lab_project.service.ManufacturerService;

@RestController
@RequestMapping("/manufacturer")
public class ManufacturerController {
    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private ManufacturerMapper manufacturerMapper;

    @Autowired
    private ProductMapper productMapper;

    @GetMapping
    public List<ManufacturerDTO> findAll(@RequestParam(required = false) String search) {
        List<Manufacturer> manufacturers = manufacturerService.findAll(search);
        return manufacturers.stream()
                .map(manufacturerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{manufacturerId}")
    public ResponseEntity<ManufacturerDTO> findById(@PathVariable Long manufacturerId) {
        Optional<Manufacturer> manufacturer = manufacturerService.findById(manufacturerId);
        return manufacturer.map(p -> ResponseEntity.ok(manufacturerMapper.toDTO(p)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ManufacturerDTO create(@RequestBody ManufacturerDTO manufacturerDTO) {
        Manufacturer manufacturer = manufacturerMapper.toEntity(manufacturerDTO);
        Manufacturer savedManufacturer = manufacturerService.save(manufacturer);
        return manufacturerMapper.toDTO(savedManufacturer);
    }

    @PutMapping("/{manufacturerId}")
    public ResponseEntity<ManufacturerDTO> update(@PathVariable Long manufacturerId, @RequestBody ManufacturerDTO manufacturerDTO) {
        if (manufacturerService.findById(manufacturerId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        manufacturerDTO.setManufacturerId(manufacturerId);
        Manufacturer manufacturer = manufacturerMapper.toEntity(manufacturerDTO);
        Manufacturer updatedManufacturer = manufacturerService.save(manufacturer);
        return ResponseEntity.ok(manufacturerMapper.toDTO(updatedManufacturer));
    }

    @DeleteMapping("/{manufacturerId}")
    public ResponseEntity<Void> delete(@PathVariable Long manufacturerId) {
        if (manufacturerService.findById(manufacturerId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        manufacturerService.deleteById(manufacturerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{manufacturerId}/unsold-products")
    public ResponseEntity<List<ProductDTO>> getUnsoldProducts(@PathVariable Long manufacturerId) {
        if (manufacturerService.findById(manufacturerId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Product> unsoldProducts = manufacturerService.getUnsoldProductsByManufacturer(manufacturerId);
        List<ProductDTO> dtos = unsoldProducts.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{manufacturerId}/sold-products")
    public ResponseEntity<Void> deleteSoldProducts(@PathVariable Long manufacturerId) {
        if (manufacturerService.findById(manufacturerId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        manufacturerService.deleteSoldProductsByManufacturer(manufacturerId);
        return ResponseEntity.noContent().build();
    }
}
