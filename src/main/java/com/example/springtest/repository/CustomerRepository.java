package com.example.springtest.repository;

import com.example.springtest.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

  @Query("SELECT c FROM Customer c WHERE c.firstName IN (:names)")
  List<Customer> findByFirstNameIn(List<String> names);

  Customer findByCpfOrUsername(String cpf, String username);
}
