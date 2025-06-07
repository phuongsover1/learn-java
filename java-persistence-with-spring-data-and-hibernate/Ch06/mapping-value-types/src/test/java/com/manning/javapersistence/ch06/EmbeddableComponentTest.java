package com.manning.javapersistence.ch06;

import com.manning.javapersistence.ch06.configuration.SpringDataConfiguration;
import com.manning.javapersistence.ch06.model.Address;
import com.manning.javapersistence.ch06.model.City;
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
@ContextConfiguration(classes = { SpringDataConfiguration.class })
public class EmbeddableComponentTest {
  @Autowired
  UserRepository userRepository;
  /*
   * @Test
   * public void testEmbeddableComponent() {
   * User user = new User();
   * user.setUsername("John");
   * Address address = new Address("test", "test", "test");
   * user.setHomeAddress(address);
   * 
   * userRepository.save(user);
   * List<User> users = (List<User>) userRepository.findAll();
   * 
   * assertAll(() -> assertEquals(address, users.get(0).getHomeAddress()));
   * }
   * 
   * @Test
   * void testOverrideEmbeddableAttributes() {
   * User user = new User();
   * user.setUsername("John");
   * Address homeAddress = new Address("test", "test", "test");
   * Address billingAddress = new Address("bill_test", "bill_test", "bill_test");
   * user.setHomeAddress(homeAddress);
   * user.setBillingAddress(billingAddress);
   * 
   * userRepository.save(user);
   * 
   * List<User> users = (List<User>) userRepository.findAll();
   * 
   * assertAll(
   * () -> assertEquals(homeAddress, users.get(0).getHomeAddress()),
   * () -> assertEquals(billingAddress, users.get(0).getBillingAddress()));
   * }
   */

  @Test
  void testMappingNestedEmbeddedComponents() {
    User user = new User();
    user.setUsername("user");
    City city = new City("city_test", "city_test", "city_test");
    City billingCity = new City("billing_city_test", "billing_city_test", "billing_city_test");
    Address homeAddress = new Address("home_test", city);
    user.setHomeAddress(homeAddress);
    Address billingAddress = new Address("billing_test", billingCity);
    user.setBillingAddress(billingAddress);

    userRepository.save(user);

    List<User> users = (List<User>) userRepository.findAll();
    assertAll(
        () -> assertEquals(homeAddress, users.get(0).getHomeAddress()),
        () -> assertEquals(billingAddress, users.get(0).getBillingAddress()));
  }
}
