package com.example.lab_project.repository;

import org.springframework.stereotype.Repository;

import com.example.lab_project.model.Client;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{
    List<Client> findByFirstNameContainingOrLastNameContainingIgnoreCase(String firstName, String lastName);
}
