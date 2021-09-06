package com.amigoscode.testing.payment;

import com.stripe.exception.StripeException;
import java.math.BigDecimal;

public interface CardPaymentCharger {
  CardPaymentCharge chargeCard( String source, BigDecimal amount, Currency currency,String description);
}
