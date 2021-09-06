package com.amigoscode.testing.customer;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends CrudRepository<Customer, UUID> {

  //Now this is a unit and we want to test it in isolation
  @Query(value = "select id, name, phone_number from customer where phone_number = :phone_number", nativeQuery = true)
  Optional<Customer> selectCustomerByPhoneNumber( @Param("phone_number") String phoneNumber);


}
