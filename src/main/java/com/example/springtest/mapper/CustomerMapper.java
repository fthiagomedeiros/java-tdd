package com.example.springtest.mapper;

import com.example.springtest.domain.Customer;
import com.example.springtest.domain.CustomerDTO;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDTO mapToCustomerDTO(Customer customer);

    Customer mapToCustomer(CustomerDTO customerDTO);

    List<CustomerDTO> toCustomerDtoList(List<Customer> customers);
}