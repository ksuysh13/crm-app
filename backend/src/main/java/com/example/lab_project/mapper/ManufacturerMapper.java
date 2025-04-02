package com.example.lab_project.mapper;

import org.springframework.stereotype.Component;

import com.example.lab_project.dto.ManufacturerDTO;
import com.example.lab_project.model.Manufacturer;

@Component
public class ManufacturerMapper {
    public ManufacturerDTO toDTO(Manufacturer manufacturer) {
        ManufacturerDTO dto = new ManufacturerDTO();
        dto.setManufacturerId(manufacturer.getManufacturerId());
        dto.setManufacturerName(manufacturer.getManufacturerName());
        dto.setCountry(manufacturer.getCountry());
        return dto;
    }

    public Manufacturer toEntity(ManufacturerDTO dto){
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setManufacturerId(dto.getManufacturerId());
        manufacturer.setManufacturerName(dto.getManufacturerName());
        manufacturer.setCountry(dto.getCountry());
        return manufacturer;
    }
}
