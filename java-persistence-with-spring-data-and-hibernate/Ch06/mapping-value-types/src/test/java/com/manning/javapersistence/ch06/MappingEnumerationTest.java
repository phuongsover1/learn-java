package com.manning.javapersistence.ch06;

import com.manning.javapersistence.ch06.configuration.SpringDataConfiguration;
import com.manning.javapersistence.ch06.model.AuctionType;
import com.manning.javapersistence.ch06.model.Item;
import com.manning.javapersistence.ch06.repositories.ItemRepository;
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
public class MappingEnumerationTest {
  @Autowired ItemRepository repo;

  @Test
  public void testMappingEnumeration() {
    Item item = new Item();
    item.setName("Test");
    repo.save(item);

    List<Item> items = (List<Item>) repo.findAll();
    assertAll(
        () -> assertEquals(1, items.size()),
        () -> assertEquals(AuctionType.HIGHEST_BID, items.get(0).getAuctionType()));
  }
}
