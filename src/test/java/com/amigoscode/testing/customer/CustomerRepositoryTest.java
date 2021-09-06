package com.amigoscode.testing.customer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest(
    properties = {"spring.jpa.properties.javax.persistence.validation.mode= none"}
) /*Allows us to test jpa queries*/
class CustomerRepositoryTest {

  @Autowired
  private CustomerRepository underTest;

  private ArgumentCaptor<Customer> captured;

  @Test
  void itShouldSelectCustomerByPhoneNumber() {
    //Given
    //When
    //Then
  }

  @Test
  void itShouldSaveCustomer() {
    //Given
    UUID id = UUID.randomUUID();
    Customer abel = new Customer(id, "Abel", "000");

    //When
    underTest.save(abel);

    //Then
    Optional<Customer> optionalCustomer = underTest.findById(id);
    assertThat(optionalCustomer)
            .isPresent()
            .hasValueSatisfying( c -> {
/*              assertThat(c.getId()).isEqualTo(id);
              assertThat(c.getName()).isEqualTo("Abel");
              assertThat(c.getPhoneNumber()).isEqualTo("000");*/

              //BEST WAY TO DO IT
              assertThat(c).isEqualToComparingFieldByField(abel);
            });

  }

  @Test
  void itShouldNotSaveCustomerWhenNameIsNull() {
    //Given
    UUID id = UUID.randomUUID();
    Customer abel = new Customer(id, null, "000");
    //When
    assertThatThrownBy( () -> underTest.save(abel))
        .isInstanceOf(DataIntegrityViolationException.class)
        .hasMessage("not-null property references a null or transient value : com.amigoscode.testing.customer.Customer.name; nested exception is org.hibernate.PropertyValueException: not-null property references a null or transient value : com.amigoscode.testing.customer.Customer.name");

  }


  @Test
  void itShouldCustomerByPhoneNumber() {
    //Given
    UUID id = UUID.randomUUID();
    Customer abel = new Customer(id, "abel", "000");
    underTest.save(abel);
    //When
    Optional<Customer> customer = underTest.selectCustomerByPhoneNumber("000");
    //Then
    assertThat(customer).isNotNull();
    assertThat(abel).isEqualToComparingFieldByField(customer.get());
  }

  @Test
  void itShouldCustomerByPhoneNumberNull() {
    //Given
    UUID id = UUID.randomUUID();
    Customer abel = new Customer(id, "abel", "001");
    underTest.save(abel);
    //When
    Optional<Customer> customer = underTest.selectCustomerByPhoneNumber("002");
    //Then
    assertThat(customer.isEmpty()).isTrue();
  }
}