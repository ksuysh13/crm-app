package com.example.lab_project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lab_project.model.Client;
import com.example.lab_project.model.Order;
import com.example.lab_project.model.Product;
import com.example.lab_project.repository.ClientRepository;
import com.example.lab_project.repository.OrderRepository;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<Client> findAll(String search) {
        if (search != null) {
            return clientRepository.findByFirstNameContainingOrLastNameContainingIgnoreCase(search, search);
        }
        return clientRepository.findAll();
    }

    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }

    public List<Order> getUncompletedOrdersByClient(Long clientId) {
        return orderRepository.findUncompletedOrdersByClient(clientId);
    }

    // public Client updateClient(Long id, Client updatedClient) {
    //     return clientRepository.findById(id)
    //             .map(client -> {
    //                 client.setFirstName(updatedClient.getFirstName());
    //                 client.setLastName(updatedClient.getLastName());
    //                 client.setEmail(updatedClient.getEmail());
    //                 client.setPhone(updatedClient.getPhone());
    //                 client.setAddress(updatedClient.getAddress());
    //                 return clientRepository.save(client);
    //             })
    //             .orElseThrow(() -> new RuntimeException("Клиент не найден"));
    // }
}
