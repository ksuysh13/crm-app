package com.example.lab_project.mapper;

import org.springframework.stereotype.Component;

import com.example.lab_project.dto.ClientDTO;
import com.example.lab_project.model.Client;

@Component
public class ClientMapper {
    public ClientDTO toDTO(Client client){
        ClientDTO dto = new ClientDTO();
        dto.setClientId(client.getClientId());
        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        dto.setEmail(client.getEmail());
        dto.setPhone(client.getPhone());
        dto.setAddress(client.getAddress());
        return dto;
    }

    public Client toEntity(ClientDTO dto){
        Client client = new Client();
        client.setClientId(dto.getClientId());
        client.setFirstName(dto.getFirstName());
        client.setLastName(dto.getLastName());
        client.setEmail(dto.getEmail());
        client.setPhone(dto.getPhone());
        client.setAddress(dto.getAddress());
        return client;
    }
}
