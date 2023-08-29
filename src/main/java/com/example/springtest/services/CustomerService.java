package com.example.springtest.services;

import com.example.springtest.domain.Customer;
import com.example.springtest.domain.CustomerDTO;
import com.example.springtest.exceptions.CpfExistsException;
import com.example.springtest.exceptions.CustomerNotFoundException;
import com.example.springtest.mapper.CustomerMapper;
import com.example.springtest.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomerService {

    private CustomerRepository repository;

    private CustomerMapper customerMapper;

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = repository.findAll();
        return customerMapper.toCustomerDtoList(customers);
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) throws CpfExistsException {
        Customer customer = customerMapper.mapToCustomer(customerDTO);

        Customer hasCustomer = repository.findByCpfOrUsername(customerDTO.getCpf(), customerDTO.getUsername());
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
}
