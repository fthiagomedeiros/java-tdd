package com.example.springtest.services;

import com.example.springtest.domain.Address;
import com.example.springtest.domain.AddressDTO;
import com.example.springtest.domain.Customer;
import com.example.springtest.domain.CustomerDTO;
import com.example.springtest.exceptions.CpfExistsException;
import com.example.springtest.exceptions.CustomerNotFoundException;
import com.example.springtest.mapper.AddressMapper;
import com.example.springtest.mapper.CustomerMapper;
import com.example.springtest.repository.AddressRepository;
import com.example.springtest.repository.CustomerRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {

  private CustomerRepository repository;
  private AddressRepository addressRepository;

  private CustomerMapper customerMapper;
  private AddressMapper addressMapper;

  public List<CustomerDTO> getAllCustomers() {
    List<Customer> customers = repository.findAll();
    return customerMapper.toCustomerDtoList(customers);
  }

  public CustomerDTO createCustomer(CustomerDTO customerDTO) throws CpfExistsException {
    Customer customer = customerMapper.mapToCustomer(customerDTO);

    Customer hasCustomer = repository.findByCpfOrUsername(customerDTO.getCpf(),
        customerDTO.getUsername());
    if (hasCustomer != null) {
      throw new CpfExistsException();
    }

    Customer saved = repository.saveAndFlush(customer);
    return customerMapper.mapToCustomerDTO(saved);
  }

  public CustomerDTO getCustomer(UUID id) throws CustomerNotFoundException {
    Optional<Customer> customer = repository.findById(id);
    return customer
        .map(value -> customerMapper.mapToCustomerDTO(value))
        .orElseThrow(CustomerNotFoundException::new);
  }

  public void deleteCustomer(UUID id) throws CustomerNotFoundException {
    Optional<Customer> customer = repository.findById(id);

    if (customer.isPresent()) {
      Customer deletedCustomer = customer.get();
      repository.delete(deletedCustomer);
      customerMapper.mapToCustomerDTO(deletedCustomer);
      return;
    }

    throw new CustomerNotFoundException();
  }

  public AddressDTO updateCustomerAddress(UUID id, AddressDTO addressDTO) {
    Optional<Customer> c = repository.findById(id);

    if (c.isEmpty()) {
      throw new CustomerNotFoundException();
    }

    Address save = addressMapper.toAddress(addressDTO);
    save.setCustomer(c.get());
    addressRepository.save(save);

    return addressMapper.toAddressDto(save);
  }
}
