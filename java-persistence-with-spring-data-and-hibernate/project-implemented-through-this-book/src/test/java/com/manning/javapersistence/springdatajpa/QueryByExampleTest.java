package com.manning.javapersistence.springdatajpa;

import com.manning.javapersistence.springdatajpa.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueryByExampleTest extends SpringDataJpaApplicationTests {
  @Test
  void testEmailWithQueryByExample() {
    User user = new User();
    user.setEmail("@somedomain.com");

    ExampleMatcher matcher =
        ExampleMatcher.matching()
            .withIgnorePaths(
                "level",
                "active") // trong Class user thì level và active là 2 kiểu primitive -> giá trị mặc định nếu không khởi tạo -> active: false, level = 0
                          // nên là khi ta query bởi email thôi nhưng bởi vì 2 field là active, level có giá trị mặc định nên là
                          // Example tưởng là ta query trên 3 field: email, active, và level -> sai ý ban đầu của ta
                          // nên là ta ignorePath(level, active) không dùng default value khi mà query by example
                          // những field không phải là primitive thì sẽ có giá trị mặc định là null -> Example tự động không dùng giá trị field đó trong khi query
            .withMatcher("email", match -> match.endsWith());


    Example<User> example = Example.of(user, matcher);
    List<User> users = userRepository.findAll(example);

    assertEquals(6, users.size());


    // Test trường hợp ta không ignorePath khi query by example mà có các primitive field
    ExampleMatcher matcherNotIgnorePrimitive = ExampleMatcher.matching()
        .withMatcher("email", match -> match.endsWith());

    Example<User> example1 = Example.of(user, matcherNotIgnorePrimitive);
    List<User> users1 = userRepository.findAll(example1);
    assertEquals(0, users1.size());
  }

  @Test
  void testUsernameWithQueryByExample() {
    User user = new User();
    user.setUsername("be");

    ExampleMatcher matcher = ExampleMatcher.matching()
        .withIgnorePaths("level", "active")
        .withStringMatcher(ExampleMatcher.StringMatcher.STARTING)
        .withIgnoreCase();

    Example<User> example = Example.of(user, matcher);

    List<User> users = userRepository.findAll(example);
    assertEquals(1, users.size());
  }
}
