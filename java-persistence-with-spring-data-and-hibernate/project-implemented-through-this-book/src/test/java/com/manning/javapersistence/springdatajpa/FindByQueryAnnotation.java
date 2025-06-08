package com.manning.javapersistence.springdatajpa;

import com.manning.javapersistence.springdatajpa.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FindByQueryAnnotation extends SpringDataJpaApplicationTests {
  @Test
  void testNumberOfUsersByActivity() {
    int active = userRepository.findNumberOfUserByActive(true);
    int inactive = userRepository.findNumberOfUserByActive(false);

    assertAll(() -> assertEquals(8, active), () -> assertEquals(2, inactive));
  }

  @Test
  void testUsersByLevelAndActivity() {
    List<User> userList1 = userRepository.findByLevelAndActive(1, true);
    List<User> userList2 = userRepository.findByLevelAndActive(2, true);
    List<User> userList3 = userRepository.findByLevelAndActive(2, false);

    assertAll(
        () -> assertEquals(2, userList1.size()),
        () -> assertEquals(2, userList2.size()),
        () -> assertEquals(1, userList3.size()));
  }

  @Test
  void testNumberOfUsersByActivityNative() {
    int active = userRepository.findNumberOfUserByActiveNativeQuery(true);
    int inactive = userRepository.findNumberOfUserByActiveNativeQuery(false);

    assertAll(() -> assertEquals(8, active), () -> assertEquals(2, inactive));
  }

  @Test
  void testFindByAsArrayAndSort() {
    List<Object[]> userList1 = userRepository.findByAsArrayAndSort("ar", Sort.by("username"));
    List<Object[]> userList2 = userRepository.findByAsArrayAndSort("ar", Sort.by("email_length"));
    List<Object[]> userList3 = userRepository.findByAsArrayAndSort("ar", JpaSort.unsafe("LENGTH(u.username)"));
    System.out.println(userList3.get(1)[0]);
    assertAll(
        () -> assertEquals(2, userList1.size()),
        () -> assertEquals("marion", userList1.get(1)[0]),
        () -> assertEquals(2, userList2.size()),
        () -> assertEquals(21, userList2.get(0)[1]),
        () -> assertEquals(2, userList3.size()),
        () -> assertEquals("darren", userList3.get(0)[0]));
  }
}
