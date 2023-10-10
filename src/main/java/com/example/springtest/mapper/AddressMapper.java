package com.example.springtest.mapper;

import com.example.springtest.domain.Address;
import com.example.springtest.domain.AddressDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

  AddressDTO toAddressDto(Address address);

  @Mapping(target = "customer", ignore = true)
  Address toAddress(AddressDTO address);
}