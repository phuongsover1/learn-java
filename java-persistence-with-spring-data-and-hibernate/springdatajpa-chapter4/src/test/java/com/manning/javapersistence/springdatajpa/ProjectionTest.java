package com.manning.javapersistence.springdatajpa;

import com.manning.javapersistence.springdatajpa.model.UserProjection;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectionTest extends SpringDataJpaApplicationTests {
  @Test
  void testUserSummaryProjection() {
    List<UserProjection.UserSummary> userSummaries =
        userRepository.findByRegistrationDateAfter(LocalDate.of(2021, Month.JANUARY, 1));
    assertAll(
        () -> assertEquals(2, userSummaries.size()),
        () -> assertEquals("katie", userSummaries.get(0).getUsername()),
        () -> assertEquals("katie katie@somedomain.com", userSummaries.get(0).getInfo()));
  }

  @Test
  void testProjectionUsername() {
    List<UserProjection.UsernameOnly> usernameOnlies = userRepository.findByEmailLike("beth%");
    assertAll(
        () -> assertEquals(1, usernameOnlies.size()),
        () -> assertEquals("beth", usernameOnlies.get(0).getUsername()));
  }

  @Test
  void testDynamicProjection() {
    List<UserProjection.UsernameOnly> usernameOnlies =
        userRepository.findByLevel(1, UserProjection.UsernameOnly.class);
    List<UserProjection.EmailOnly> emailOnlies =
        userRepository.findByLevel(4, UserProjection.EmailOnly.class);
    assertAll(
        () -> assertEquals(2, usernameOnlies.size()),
        () -> assertEquals("john", usernameOnlies.get(0).getUsername()),
        () -> assertEquals(2, emailOnlies.size()),
        () -> assertEquals("julius@someotherdomain.com", emailOnlies.get(0).getEmail()));
  }
}
