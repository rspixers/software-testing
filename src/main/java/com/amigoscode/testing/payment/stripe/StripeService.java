package com.amigoscode.testing.payment.stripe;

import com.amigoscode.testing.payment.CardPaymentCharge;
import com.amigoscode.testing.payment.CardPaymentCharger;
import com.amigoscode.testing.payment.Currency;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StripeService implements CardPaymentCharger {

  private final static RequestOptions requestOptions = RequestOptions.builder()
      .setApiKey("sk_test_4eC39HqLyjWDarjtT1zdp7dc")
      .build();

  private final StripeApi stripeApi;

  @Autowired
  public StripeService(StripeApi stripeApi) {
    this.stripeApi = stripeApi;
  }

  @Override
  public CardPaymentCharge chargeCard(String source, BigDecimal amount, Currency currency,
      String description)  {
    // `source` is obtained with Stripe.js; see https://stripe.com/docs/payments/accept-a-payment-charges#web-create-token
    Map<String, Object> params = new HashMap<>();
    params.put("amount", amount);
    params.put("currency",currency);
    params.put("source", source);
    params.put(
        "description",
        description
    );

    Charge charge = null;
    try {
      charge = stripeApi.create(params, requestOptions);
      return new CardPaymentCharge(charge.getPaid());
    } catch (StripeException e) {
      throw new IllegalStateException("Cannot make Stripe Charges", e);
    }

  }
}
