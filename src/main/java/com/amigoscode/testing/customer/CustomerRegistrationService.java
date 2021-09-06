package com.amigoscode.testing.customer;

import java.util.Optional;
import java.util.UUID;
import javax.swing.text.html.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerRegistrationService {

  private final CustomerRepository customerRepository;

  @Autowired
  public CustomerRegistrationService(CustomerRepository customerRepository)
  {
    this.customerRepository= customerRepository;
  }

  public void registerNewCustomer(CustomerRegistrationRequest customerRegistrationRequest)
      throws IllegalAccessException {
    //1. Phone Number is taken
    //2. if Take, let's check if it belongs to the same customer
    // - 2.1 if Yes, return
    // - 2.2 Throw exception
    //5. Save Customer

    Customer newCustomer = customerRegistrationRequest.getCustomer();

    Optional<Customer> savedCustomer = customerRepository
        .selectCustomerByPhoneNumber(newCustomer.getPhoneNumber());

    if(savedCustomer.isPresent())
    {
      Customer customer = savedCustomer.get();
      if(customer.getName().equals(newCustomer.getName()))
        return;
      else
        throw new IllegalAccessException(String.format("Phone number [%s] is taken", newCustomer.getPhoneNumber()));
    }

    if(newCustomer.getId() == null)
    {
      customerRegistrationRequest.getCustomer().setId(UUID.randomUUID());
    }
    customerRepository.save(newCustomer);

  }

}
