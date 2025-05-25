package com.manning.javapersistence.ch05;

import com.manning.javapersistence.ch05.configuration.SpringDataConfiguration;
import com.manning.javapersistence.ch05.model.Item;
import com.manning.javapersistence.ch05.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDataConfiguration.class})
public class HelloSpringDataTest {
  @Autowired
  ItemRepository itemRepository;
  @Test
  void testStoreItem() {
    Item item = new Item();
    item.setName("Spring Data Stored Item");
    item.setAuctionEnd(Helper.tomorrow());

    itemRepository.save(item);

    List<Item> items =  (List<Item>) itemRepository.findAll();

    assertAll(
        () -> assertEquals(1, items.size()),
        () -> assertEquals("Spring Data Stored Item", items.get(0).getName())
    );

  }
}
