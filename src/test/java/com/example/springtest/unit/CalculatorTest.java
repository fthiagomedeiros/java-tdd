package com.example.springtest.unit;

import com.example.springtest.controllers.CustomerController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalculatorTest {

  private CustomerController controller;

  @Test
  public void testSum() {
    Assertions.assertTrue(true);
  }

  @Test
  public void testSubtract() {
    Assertions.assertEquals(1, 1);
  }

}
