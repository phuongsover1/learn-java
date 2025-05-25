package com.manning.javapersistence.ch06;

import com.manning.javapersistence.ch06.configuration.SpringDataConfiguration;
import com.manning.javapersistence.ch06.model.Item;
import com.manning.javapersistence.ch06.repositories.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDataConfiguration.class})
public class SpringDataTest {

  @Autowired
  ItemRepository itemRepository;

  @Test
  void testStoreItem() {
    Item item = new Item();
    item.setName("Test");
    item.setDescription("This item is a new item, you should buy this new item, bla bla bla bla");

    itemRepository.save(item);

    List<Item> items = (List<Item>) itemRepository.findAll();
    assertAll(
        () -> assertEquals(1, items.size()),
        () -> assertEquals("AUCTION: Test", items.get(0).getName()),
        () -> assertEquals(15, items.get(0).getShortDescription().length()),
        () -> assertTrue(items.get(0).getShortDescription().endsWith("..."))
    );
  }
}
