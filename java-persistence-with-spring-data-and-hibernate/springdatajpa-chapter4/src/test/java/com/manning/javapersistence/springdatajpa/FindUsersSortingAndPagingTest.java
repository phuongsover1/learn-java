package com.manning.javapersistence.springdatajpa;

import com.manning.javapersistence.springdatajpa.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FindUsersSortingAndPagingTest extends SpringDataJpaApplicationTests {

    @Test
    void testOrder() {
        User user1 = userRepository.findFirstByOrderByUsernameAsc();
        User user2 = userRepository.findTopByOrderByRegistrationDateDesc();
        Page<User> userPage = userRepository.findAll(PageRequest.of(1, 3));
        List<User> users = userRepository.findFirst2ByLevel(2, Sort.by("registrationDate"));

        assertAll(
                () -> assertEquals("beth", user1.getUsername()),
                () -> assertEquals("julius", user2.getUsername()),
                () -> assertEquals(3, userPage.getSize()),
                () -> assertEquals(2, users.size()),
                () -> assertEquals("beth", users.get(0).getUsername()),
                () -> assertEquals("marion", users.get(1).getUsername())
        );
    }

    @Test
    void testFindByLevelUsingSortCriteria() {
        Sort.TypedSort<User> user = Sort.sort(User.class);

        List<User> users = userRepository.findByLevel(2,
                user.by(User::getRegistrationDate).descending());

        assertAll(
                () -> assertEquals(3, users.size()),
                () -> assertEquals("darren", users.get(0).getUsername())
        );
    }

    @Test
    void testFindByActive() {
        List<User> userList1 = userRepository.findByActive(true,
                PageRequest.of(0, 4, Sort.by("username")));
        List<User> userList2 = userRepository.findByActive(true,
                PageRequest.of(1, 4, Sort.by("username")));

        /*
        john
        mike
        katie
        beth
        julius
        darren
        stephanie
        burk

        --> list 1
        beth
        burk
        darren
        john

        ---> list 2
        julius
        katie
        mike
        stephanie
         */
        assertAll(
                () -> assertEquals(4, userList1.size()),
                () -> assertEquals(4, userList2.size()),
                () -> assertEquals("beth", userList1.get(0).getUsername()),
                () -> assertEquals("burk", userList1.get(1).getUsername()),
                () -> assertEquals("darren", userList1.get(2).getUsername()),
                () -> assertEquals("john", userList1.get(3).getUsername()),
                () -> assertEquals("stephanie", userList2.get(userList2.size() - 1).getUsername())
        );
    }
}
