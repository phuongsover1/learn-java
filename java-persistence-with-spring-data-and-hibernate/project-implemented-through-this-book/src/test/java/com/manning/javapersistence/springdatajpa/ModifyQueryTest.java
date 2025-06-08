package com.manning.javapersistence.springdatajpa;

import com.manning.javapersistence.springdatajpa.model.User;
import com.manning.javapersistence.springdatajpa.model.UserProjection;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModifyQueryTest extends SpringDataJpaApplicationTests {
  @Test
  void testUpdateLevel() {
    int updated = userRepository.updateLevel(4, 5);
    int nonUpdated = userRepository.updateLevel(7, 8);
    List<UserProjection.UsernameOnly> users = userRepository.findByLevel(5, UserProjection.UsernameOnly.class);

    assertAll(
        () -> assertEquals(2, updated), // Because we have 2 user with level 4
        () -> assertEquals(0, nonUpdated),
        () -> assertEquals(3, users.size()), // Now we have 3 user with level 5 : old -> 1, new -> 2
        () -> assertEquals("katie", users.get(0).getUsername()));
  }

  @Test
  void testDeleteByLevel() {
    int deleted = userRepository.deleteByLevel(2);
    List<User> users = userRepository.findByLevel(2, Sort.by("username"));

    assertAll(() -> assertEquals(3, deleted), () -> assertEquals(0, users.size()));
  }

  @Test
  void testDeleteBulkByLevel() {
    int deleted = userRepository.deleteBulkByLevel(1);
    List<User> users = userRepository.findByLevel(1, Sort.by("username"));
    assertEquals(0, users.size());
  }
}
