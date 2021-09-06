package com.amigoscode.testing.payment;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest(
    properties = {"spring.jpa.properties.javax.persistence.validation.mode= none"}
) /*Allows us to test jpa queries*/
  
class PaymentRepositoryTest {
  
  @Autowired
  private PaymentRepository underTest;

  @Test
  void itShouldInsertPayment() {
    //Given
    Payment payment = new Payment(1L, UUID.randomUUID(), new BigDecimal("10.00"), Currency.USD, "card123", "Donation");
    //When
    underTest.save(payment);
    //Then
    Optional<Payment> capturedPayment = underTest.findById(1L);
    assertThat(capturedPayment.isEmpty()).isFalse();

    Payment p = capturedPayment.get();
    assertThat(p).isEqualToComparingFieldByField(payment);
  }
}