package com.manning.javapersistence.springdatajpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.manning.javapersistence.springdatajpa.model.Item;

public class ConvertMonetaryAmountTest extends SpringDataJpaApplicationTests {

  @Test
  void testConvertMonetaryAmount() {
    List<Item> items = (List<Item>) itemRepository.findAll();

    assertNotNull(items);
    assertNotEquals(0, items.size());
    assertEquals("USD", items.get(0).getBuyNowPrice().getCurrency().toString());
    assertEquals("2.20 USD", items.get(0).getBuyNowPrice().toString());
    assertEquals("4.40 USD", items.get(1).getBuyNowPrice().toString());
    assertEquals("2.00 EUR", items.get(0).getInitialPrice().toString());
    assertEquals("4.00 EUR", items.get(1).getInitialPrice().toString());
  }
}
