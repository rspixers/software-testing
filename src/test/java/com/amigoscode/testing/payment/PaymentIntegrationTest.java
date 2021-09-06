package com.amigoscode.testing.payment;





import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.amigoscode.testing.customer.Customer;
import com.amigoscode.testing.customer.CustomerRegistrationController;
import com.amigoscode.testing.customer.CustomerRegistrationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest /*Makes sure that when we run any test within this class the entire application will startup and die*/
@AutoConfigureMockMvc/*Autowired the application being run*/
class PaymentIntegrationTest {

  /*We don't autowire because when we autowire we are NOT testing the endpoint but directly testing the function inside the controller
  * WE SHOULD TEST THE ENDPOINT*/
//  @Autowired
//  private CustomerRegistrationController customerRegistrationController;

  /*The application that is running*/
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private PaymentRepository paymentRepository;

  @Test
  void itShouldCreatePaymentSuccessfully() throws Exception {
    //Given
    UUID uuid = UUID.randomUUID();
    Customer customer = new Customer(uuid, "James", "0000000000");

    ResultActions customerRegResultAction = mockMvc.perform(put("/api/v1/customer/registration").contentType(MediaType.APPLICATION_JSON).content(objectToJson(new CustomerRegistrationRequest(customer))));
    System.out.println(customerRegResultAction);
    Payment payment = new Payment(1L, uuid, new BigDecimal("1000.00"), Currency.USD, "Bank", "Salary");
    //When
    ResultActions paymentResultAction = mockMvc.perform(
        post("/api/v1/payment", uuid).contentType(MediaType.APPLICATION_JSON)
            .content(objectToJson(new PaymentRequest(payment))));
    //Then
    customerRegResultAction.andExpect(status().isOk());
    paymentResultAction.andExpect(status().isOk());

    assertThat(paymentRepository.findById(1L)).isPresent()
        .hasValueSatisfying( p-> assertThat(p).isEqualToComparingFieldByField(payment));
  }



  private String objectToJson(Object object)
  {
    try{
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      fail("Failed to convert Object to Json");
      return null;
    }
  }
}
