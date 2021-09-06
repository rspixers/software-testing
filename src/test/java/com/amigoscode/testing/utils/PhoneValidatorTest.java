package com.amigoscode.testing.utils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PhoneValidatorTest {


  private PhoneNumberValidator underTest;

  @BeforeEach
  void setUp() {
      underTest = new PhoneNumberValidator();
  }

  @ParameterizedTest
  @CsvSource({"+440000000000, true"})
  void itShouldValidatePhoneNumberWhenCorrect(String input, boolean expected) {
    //Given
    String phoneNumber = input;
    //When
    Boolean isValid = underTest.test(phoneNumber);
    //Then
    assertThat(isValid).isEqualTo(expected);
  }

  @Test
  @DisplayName("Should fail when length is bigger than 13")
  void itShouldValidatePhoneNumberWhenIncorrectBasedOnSize() {
    //Given
    String phoneNumber = "+44000000000010";
    //When
    Boolean isValid = underTest.test(phoneNumber);
    //Then
    assertThat(isValid).isFalse();
  }




}
