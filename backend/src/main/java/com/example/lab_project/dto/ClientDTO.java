package com.example.lab_project.dto;

import lombok.Data;

@Data
public class ClientDTO {
    private Long clientId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
}
