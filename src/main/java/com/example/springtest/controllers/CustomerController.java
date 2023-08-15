package com.example.springtest.controllers;

import com.example.springtest.domain.CustomerDTO;
import com.example.springtest.exceptions.CustomerNotFoundException;
import com.example.springtest.services.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/customer")
@AllArgsConstructor
public class CustomerController {

    private CustomerService service;

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> fetchAllCustomers() {
        List<CustomerDTO> customers = service.getAllCustomers();
        return ResponseEntity.ok().body(customers);
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody @Valid CustomerDTO mCustomer,
        UriComponentsBuilder uriComponentsBuilder) {
        CustomerDTO customer = service.createCustomer(mCustomer);
        return ResponseEntity.created(uriComponentsBuilder.path("/customer/{customerId}")
            .build(customer.getId())).build();
    }

    @GetMapping({"{id}"})
    public ResponseEntity<CustomerDTO> fetchCustomerById(@PathVariable String id) {
        CustomerDTO customer = service.getCustomer(id);
        return ResponseEntity.ok().body(customer);
    }

    @DeleteMapping({"{id}"})
    public ResponseEntity<CustomerDTO> deleteCustomer(@PathVariable String id) throws CustomerNotFoundException {
        service.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

}
