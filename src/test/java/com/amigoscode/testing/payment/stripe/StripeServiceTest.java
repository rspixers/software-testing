package com.amigoscode.testing.payment.stripe;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.amigoscode.testing.payment.Currency;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class StripeServiceTest {

  private StripeService underTest;

  @Mock
  private StripeApi stripeApi;

  @BeforeEach
  void setUp()
  {

    MockitoAnnotations.initMocks(this);
    underTest = new StripeService(stripeApi);
  }

  @Test
  void itShouldChargeCard() throws StripeException {
    //Given
    //When
    String source = "";
    BigDecimal amount = new BigDecimal("10");
    String heloo = "Heloo";
    Charge charge = new Charge();
    charge.setPaid(true);
    given(stripeApi.create(any(),any())).willReturn(charge);

    underTest.chargeCard(source, amount, Currency.USD, heloo);
    //Then
    ArgumentCaptor<Map<String,Object>> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
    ArgumentCaptor<RequestOptions> optionsArgumentCaptor = ArgumentCaptor.forClass(RequestOptions.class);

    then(stripeApi).should().create(mapArgumentCaptor.capture(),optionsArgumentCaptor.capture());

    Map<String, Object> requestMap = mapArgumentCaptor.getValue();

    assertThat(requestMap.size()).isEqualTo(4);
  }
}