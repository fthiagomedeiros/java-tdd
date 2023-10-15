package com.example.springtest.mapper;

import com.example.springtest.domain.Address;
import com.example.springtest.domain.AddressDTO;
import com.example.springtest.domain.Customer;
import com.example.springtest.domain.CustomerDTO;
import java.util.List;
import java.util.Optional;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface CustomerMapper {

  @Mapping(target = "addressDTO", source = "customer.address")
  CustomerDTO mapToCustomerDTO(Customer customer);

  @Mapping(target = "address", source = "customerDTO.addressDTO")
  Customer mapToCustomer(CustomerDTO customerDTO);

  List<CustomerDTO> toCustomerDtoList(List<Customer> customers);

  @AfterMapping
  default void setCustomerAddressBidirectionalRelation(@MappingTarget Customer customer) {
    customer.getAddress().setCustomer(customer);
  }
}