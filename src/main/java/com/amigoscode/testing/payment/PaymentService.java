package com.amigoscode.testing.payment;

import com.amigoscode.testing.customer.Customer;
import com.amigoscode.testing.customer.CustomerRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final CustomerRepository customerRepository;
  private final CardPaymentCharger cardPaymentCharger;

  private static final List<Currency> ACCEPTED_CURRENCY= Arrays.asList( Currency.USD, Currency.GBP);

  public PaymentService(PaymentRepository paymentRepository,
      CustomerRepository customerRepository,
      CardPaymentCharger cardPaymentCharger) {
    this.paymentRepository = paymentRepository;
    this.customerRepository = customerRepository;
    this.cardPaymentCharger = cardPaymentCharger;
  }


  public void chargeCard(UUID customerId, PaymentRequest paymentRequest)
  {
    Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
    Payment payment = paymentRequest.getPayment();

    if(optionalCustomer.isEmpty())
      throw new IllegalStateException("No customer Id found "+ customerId);

    if(!ACCEPTED_CURRENCY.contains(payment.getCurrency()))
        throw new IllegalStateException("No currency found for " + payment.getCurrency());

    CardPaymentCharge cardPaymentCharge = cardPaymentCharger.chargeCard(payment.getSource(), payment.getAmount(), payment.getCurrency(), payment.getDescription());

    if(!cardPaymentCharge.isCardDebited())
      throw new IllegalStateException(String.format("Card not Debited for %s",customerId));

    payment.setCustomerId(customerId);
    paymentRepository.save(payment);


  }

}
