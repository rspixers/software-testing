package com.amigoscode.testing.payment.stripe;

import com.amigoscode.testing.payment.CardPaymentCharge;
import com.amigoscode.testing.payment.CardPaymentCharger;
import com.amigoscode.testing.payment.Currency;
import java.math.BigDecimal;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "stripe.enabled", havingValue = "false") /*For Integration Testing*/
public class MockStripeService implements CardPaymentCharger {

  @Override
  public CardPaymentCharge chargeCard(String source, BigDecimal amount, Currency currency,
      String description) {
    CardPaymentCharge cardPaymentCharge = new CardPaymentCharge(true);
    return cardPaymentCharge;
  }
}
