package com.amigoscode.testing.customer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import java.util.Optional;
import java.util.UUID;
import javax.swing.text.html.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class CustomerRegistrationServiceTest {

  @Mock
  private CustomerRepository customerRepository;

  @Captor
  private ArgumentCaptor<Customer> customerArgumentCaptor;

  private CustomerRegistrationService underTest;

  @BeforeEach
  void SetUp()
  {
    MockitoAnnotations.initMocks(this);
    underTest = new CustomerRegistrationService(customerRepository);

  }

  @Test
  void itShouldSaveNewCustomer() throws IllegalAccessException {
    //Given a phone number and a customer
    String phoneNumber = "000099";
    Customer customer = new Customer(UUID.randomUUID(), "Maryam", phoneNumber);

    //.. a request
    CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);
    given(customerRepository.selectCustomerByPhoneNumber(phoneNumber)).willReturn(Optional.empty());

    //When
    underTest.registerNewCustomer(request);

    //Then
    then(customerRepository).should().save(customerArgumentCaptor.capture());
    Customer capturedValue = customerArgumentCaptor.getValue();
    assertThat(capturedValue).isEqualToComparingFieldByField(customer);
  }

  @Test
  void itShouldNotSaveCustomerWhenCustomerExist() throws IllegalAccessException {
    //Given
    //Given a phone number and a customer
    String phoneNumber = "000099";
    Customer customer = new Customer(UUID.randomUUID(), "Maryam", phoneNumber);
    //.. a request
    CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);
    given(customerRepository.selectCustomerByPhoneNumber(phoneNumber)).willReturn(Optional.of(customer));
    //When
    underTest.registerNewCustomer(request);

    //Then
    then(customerRepository).should(never()).save(any());
//    then(customerRepository).should().selectCustomerByPhoneNumber(any());
//    then(customerRepository).shouldHaveNoMoreInteractions();
  }

  @Test
  void itShouldNotSaveCustomerWithCustomer() throws IllegalAccessException {
    //Given
    //Given
    //Given a phone number and a customer
    String phoneNumber = "000099";
    Customer customer = new Customer(UUID.randomUUID(), "Maryam", phoneNumber);
    Customer existingCustomer = new Customer(UUID.randomUUID(), "Jamal", phoneNumber);
    //.. a request
    CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);
    given(customerRepository.selectCustomerByPhoneNumber(phoneNumber)).willReturn(Optional.of(existingCustomer));
    //When
    assertThatThrownBy(() -> underTest.registerNewCustomer(request))
          .isInstanceOf(IllegalAccessException.class)
          .hasMessage(String.format("Phone number [%s] is taken", phoneNumber));
    //Then
    then(customerRepository).should(never()).save(any());


  }

  @Test
  void itShouldSaveNewCustomerWhenIdIsNull() throws IllegalAccessException {
    //Given a phone number and a customer
    String phoneNumber = "000099";
    Customer customer = new Customer(null, "Maryam", phoneNumber);

    //.. a request
    CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);
    given(customerRepository.selectCustomerByPhoneNumber(phoneNumber)).willReturn(Optional.empty());

    //When
    underTest.registerNewCustomer(request);

    //Then
    then(customerRepository).should().save(customerArgumentCaptor.capture());
    Customer capturedValue = customerArgumentCaptor.getValue();
    assertThat(capturedValue.getId()).isNotNull();
    assertThat(capturedValue).isEqualToComparingFieldByField(customer);
    assertThat(capturedValue).isEqualToIgnoringGivenFields(customer, "id");
  }



}