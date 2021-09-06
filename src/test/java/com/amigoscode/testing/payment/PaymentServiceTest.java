package com.amigoscode.testing.payment;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.amigoscode.testing.customer.Customer;
import com.amigoscode.testing.customer.CustomerRepository;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import javax.swing.text.html.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PaymentServiceTest {

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private CardPaymentCharger cardPaymentCharger;

  @Mock
  private PaymentRepository paymentRepository;

  private PaymentService underTest;

  private ArgumentCaptor<Payment> capturedPayment;

  @BeforeEach
  public void setup()
  {
    capturedPayment = ArgumentCaptor.forClass(Payment.class);
    MockitoAnnotations.initMocks(this);
    underTest = new PaymentService(paymentRepository, customerRepository, cardPaymentCharger);
  }

  @Test
  void itShouldChargeCardSuccessfully() {
    //Given
    UUID id = UUID.randomUUID();
    Customer customer = new Customer(id, "Jamal", "008");
    Payment payment = new Payment(1L, null, new BigDecimal("1000.00"), Currency.USD, "Icici bank", "Salary");
    PaymentRequest paymentRequest = new PaymentRequest(payment);
    CardPaymentCharge cardPaymentCharge = new CardPaymentCharge(true);

    given(customerRepository.findById(id)).willReturn(Optional.of(customer));
    given(cardPaymentCharger.chargeCard(payment.getSource(),payment.getAmount(),payment.getCurrency(), payment.getDescription())).willReturn(cardPaymentCharge).willReturn(cardPaymentCharge);


    //When

    underTest.chargeCard(id, paymentRequest);
    //Then

    then(paymentRepository).should().save(capturedPayment.capture());
    Payment p = capturedPayment.getValue();
    assertThat(p).isEqualToIgnoringGivenFields(payment, "customerId");
    assertThat(p.getCustomerId()).isEqualTo(id);
  }

  @Test
  void itShouldThrowWhenCardIsNotCharged() {
    //Given
    UUID id = UUID.randomUUID();
    Customer customer = new Customer(id, "Jamal", "008");
    Payment payment = new Payment(1L, null, new BigDecimal("1000.00"), Currency.USD, "Icici bank", "Salary");
    PaymentRequest paymentRequest = new PaymentRequest(payment);
    CardPaymentCharge cardPaymentCharge = new CardPaymentCharge(false);

    given(customerRepository.findById(id)).willReturn(Optional.of(customer));
    given(cardPaymentCharger.chargeCard(payment.getSource(),payment.getAmount(),payment.getCurrency(), payment.getDescription())).willReturn(cardPaymentCharge).willReturn(cardPaymentCharge);


    //When

    assertThatThrownBy( () -> underTest.chargeCard(id, paymentRequest))
            .isInstanceOf(IllegalStateException.class);

    //Then

    then(paymentRepository).should(never()).save(any());

  }

  @Test
  void itShouldNotChargeCardWhenCurrencyNotSupportedAndThrow() {
    //Given
    UUID id = UUID.randomUUID();
    Customer customer = new Customer(id, "Jamal", "008");
    Payment payment = new Payment(1L, null, new BigDecimal("1000.00"), Currency.EUR, "Icici bank", "Salary");
    PaymentRequest paymentRequest = new PaymentRequest(payment);
    CardPaymentCharge cardPaymentCharge = new CardPaymentCharge(true);

    given(customerRepository.findById(id)).willReturn(Optional.of(customer));

    //When

    assertThatThrownBy( () -> underTest.chargeCard(id, paymentRequest))
        .isInstanceOf(IllegalStateException.class);

    //Then

    then(paymentRepository).shouldHaveNoInteractions();
    then(cardPaymentCharger).shouldHaveNoInteractions();
  }

  @Test
  void itShouldNotChargeAndCustomerNotFoundThrow() {
    //Given
    UUID id = UUID.randomUUID();
    Customer customer = new Customer(id, "Jamal", "008");
    Payment payment = new Payment(1L, null, new BigDecimal("1000.00"), Currency.EUR, "Icici bank", "Salary");
    PaymentRequest paymentRequest = new PaymentRequest(payment);
    CardPaymentCharge cardPaymentCharge = new CardPaymentCharge(true);

    given(customerRepository.findById(id)).willReturn(Optional.empty());

    //When

    assertThatThrownBy( () -> underTest.chargeCard(id, paymentRequest))
        .isInstanceOf(IllegalStateException.class);

    //Then

    then(paymentRepository).shouldHaveNoInteractions();
    then(cardPaymentCharger).shouldHaveNoInteractions();
  }
}