package com.manning.javapersistence.springdatajpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.Month;

import org.junit.jupiter.api.Test;
import org.springframework.orm.jpa.JpaSystemException;

import com.github.javafaker.Faker;
import com.manning.javapersistence.springdatajpa.model.Address;
import com.manning.javapersistence.springdatajpa.model.City;
import com.manning.javapersistence.springdatajpa.model.GermanZipcode;
import com.manning.javapersistence.springdatajpa.model.SwissZipcode;
import com.manning.javapersistence.springdatajpa.model.User;

public class ConvertZipcodeFromDBAndViceVersaTest extends SpringDataJpaApplicationTests {

  @Test
  public void getZipcodeFromDB() {
    User john = userRepository.findByUsername("john");
    assertNotNull(john);
    assertEquals("12345", john.getHomeAddress().getCity().getZipcode().getValue());
    assertInstanceOf(GermanZipcode.class, john.getHomeAddress().getCity().getZipcode());

    User mike = userRepository.findByUsername("mike");
    assertNotNull(mike);
    assertEquals("1234", mike.getHomeAddress().getCity().getZipcode().getValue());
    assertInstanceOf(SwissZipcode.class, mike.getHomeAddress().getCity().getZipcode());

    // Exception unsupportZipcodeException =
    // assertThrows(IllegalArgumentException.class, () -> {
    // userRepository.findByUsername("james");
    // });
    // String expectedMessage = "Unsupported zipcode in database";
    // assertTrue(unsupportZipcodeException.getMessage().contains(expectedMessage));
  }

  @Test
  public void saveUnsupportZipCode() {
    Faker faker = new Faker();
    User james = new User("james", LocalDate.of(2020, Month.MARCH, 11));
    james.setEmail("james@someotherdomain.com");
    james.setLevel(3);
    james.setActive(false);
    james.setHomeAddress(new Address(faker.address().streetAddress(),
        new City(new SwissZipcode("123456"), faker.country().name(), faker.address().cityName())));
    assertThrows(JpaSystemException.class, () -> {
      userRepository.save(james);
    });

  }
}
