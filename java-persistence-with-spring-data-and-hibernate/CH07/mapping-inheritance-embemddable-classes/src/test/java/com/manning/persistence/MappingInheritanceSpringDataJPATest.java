package com.manning.persistence;

import com.manning.persistence.ch07.repositories.ItemRepository;
import com.manning.persistence.configuration.SpringDataConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDataConfiguration.class})
public class MappingInheritanceSpringDataJPATest {
  @Autowired private ItemRepository itemRepository;

  @Test
  public void findAllItem() {
    itemRepository.findAll();
  }
}
