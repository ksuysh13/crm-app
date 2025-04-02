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

import com.example.lab_project.dto.ClientDTO;
import com.example.lab_project.dto.OrderDTO;
import com.example.lab_project.mapper.ClientMapper;
import com.example.lab_project.mapper.OrderMapper;
import com.example.lab_project.model.Client;
import com.example.lab_project.model.Order;
import com.example.lab_project.service.ClientService;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private OrderMapper orderMapper;

    @GetMapping
    public List<ClientDTO> findAll(@RequestParam(required = false) String search) {
        List<Client> clients = clientService.findAll(search);
        return clients.stream()
                .map(clientMapper::toDTO)
                .collect(Collectors.toList());
    }

     @GetMapping("/{clientId}")
    public ResponseEntity<ClientDTO> findById(@PathVariable Long clientId) {
        Optional<Client> client = clientService.findById(clientId);
        return client.map(p -> ResponseEntity.ok(clientMapper.toDTO(p)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ClientDTO create(@RequestBody ClientDTO clientDTO) {
        Client client = clientMapper.toEntity(clientDTO);
        Client savedClient = clientService.save(client);
        return clientMapper.toDTO(savedClient);
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<ClientDTO> update(@PathVariable Long clientId, @RequestBody ClientDTO clientDTO) {
        if (clientService.findById(clientId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        clientDTO.setClientId(clientId);
        Client client = clientMapper.toEntity(clientDTO);
        Client updatedClient = clientService.save(client);
        return ResponseEntity.ok(clientMapper.toDTO(updatedClient));
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> delete(@PathVariable Long clientId) {
        if (clientService.findById(clientId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        clientService.deleteById(clientId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{clientId}/uncompleted-orders")
    public ResponseEntity<List<OrderDTO>> getUncompletedOrders(@PathVariable Long clientId) {
        if (clientService.findById(clientId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Order> uncompletedOrders = clientService.getUncompletedOrdersByClient(clientId);
        List<OrderDTO> dtos = uncompletedOrders.stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
