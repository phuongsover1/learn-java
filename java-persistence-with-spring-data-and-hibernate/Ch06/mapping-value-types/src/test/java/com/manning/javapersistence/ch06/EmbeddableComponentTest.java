package com.manning.javapersistence.ch06;

import com.manning.javapersistence.ch06.configuration.SpringDataConfiguration;
import com.manning.javapersistence.ch06.model.Address;
import com.manning.javapersistence.ch06.model.Item;
import com.manning.javapersistence.ch06.model.User;
import com.manning.javapersistence.ch06.repositories.UserRepository;
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
public class EmbeddableComponentTest {
  @Autowired
  UserRepository userRepository;

  @Test
  public void testEmbeddableComponent() {
    User user = new User();
    user.setUsername("John");
    Address address = new Address("test", "test", "test");
    user.setHomeAddress(address);

    userRepository.save(user);
    List<User> users = (List<User>) userRepository.findAll();

    assertAll(() -> assertEquals(address, users.get(0).getHomeAddress()));
  }
}
