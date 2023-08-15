package com.example.springtest.services;

import com.example.springtest.domain.Customer;
import com.example.springtest.domain.CustomerDTO;
import com.example.springtest.exceptions.CustomerNotFoundException;
import com.example.springtest.mapper.CustomerMapper;
import com.example.springtest.repository.CustomerRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {

    private CustomerRepository repository;

    private CustomerMapper customerMapper;

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = repository.findAll();
        return customerMapper.toCustomerDtoList(customers);
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.mapToCustomer(customerDTO);
        Customer saved = repository.save(customer);
        return customerMapper.mapToCustomerDTO(saved);
    }

    public CustomerDTO getCustomer(String id) {
        Optional<Customer> customer = repository.findById(id);
        return customer.map(value -> customerMapper.mapToCustomerDTO(value)).orElse(null);
    }

    public CustomerDTO deleteCustomer(String id) throws CustomerNotFoundException {
        Optional<Customer> customer = repository.findById(id);

        if (customer.isPresent()) {
            Customer deletedCustomer = customer.get();
            repository.delete(deletedCustomer);
            return customerMapper.mapToCustomerDTO(deletedCustomer);
        }

        throw new CustomerNotFoundException();
    }
}
