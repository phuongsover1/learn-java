package com.manning.javapersistence.springdatajpa;

import com.manning.javapersistence.springdatajpa.model.User;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FindUserStreamResult extends SpringDataJpaApplicationTests {
  @Test
  void testStreamable() {
    Stream<User> results =
        userRepository
            .findByEmailContaining("someother")
            .and(userRepository.findByLevel(2))
            .stream()
            .distinct();
    assertEquals(6, results.count());
    results.close(); // nếu không dùng close ở đây thì connection đến db vẫn bị giữ

    // USING TRY-WITH-RESOURCE
    try (Stream<User> result2 =
        userRepository
            .findByEmailContaining("someother")
            .and(userRepository.findByLevel(2))
            .stream()
            .distinct()) {
      assertEquals(6, result2.count());
    }
  }
}
