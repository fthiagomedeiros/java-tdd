package com.example.springtest.controllers;

import com.example.springtest.domain.AddressDTO;
import com.example.springtest.domain.CustomerDTO;
import com.example.springtest.exceptions.CpfExistsException;
import com.example.springtest.exceptions.CustomerNotFoundException;
import com.example.springtest.services.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/customer")
public class CustomerController {

  private final CustomerService service;

  @GetMapping
  public ResponseEntity<List<CustomerDTO>> fetchAllCustomers() {
    List<CustomerDTO> customers = service.getAllCustomers();
    return ResponseEntity.ok().body(customers);
  }

  @PostMapping
  public ResponseEntity<CustomerDTO> createCustomer(@RequestBody @Valid CustomerDTO mCustomer,
      UriComponentsBuilder uriComponentsBuilder) throws CpfExistsException {
    CustomerDTO customer = service.createCustomer(mCustomer);
    return ResponseEntity.created(uriComponentsBuilder.path("/customer/{customerId}")
        .build(customer.getId())).build();
  }

  @PutMapping({"{id}"})
  public ResponseEntity<AddressDTO> updateCustomerAddress(@PathVariable UUID id,
                                                       @RequestBody AddressDTO addressDTO,
                                                       UriComponentsBuilder uriComponentsBuilder) {
    AddressDTO address = service.updateCustomerAddress(id, addressDTO);
    return ResponseEntity.created(uriComponentsBuilder.path("/address/{addressId}")
        .build(address.getId())).body(address);
  }

  @GetMapping({"{id}"})
  public ResponseEntity<CustomerDTO> fetchCustomerById(@PathVariable UUID id)
      throws CustomerNotFoundException {
    CustomerDTO customer = service.getCustomer(id);
    return ResponseEntity.ok().body(customer);
  }

  @DeleteMapping({"{id}"})
  public ResponseEntity<CustomerDTO> deleteCustomer(@PathVariable UUID id)
      throws CustomerNotFoundException {
    service.deleteCustomer(id);
    return ResponseEntity.noContent().build();
  }

}
