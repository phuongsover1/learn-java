package com.manning.javapersistence.springdatajpa;

import com.manning.javapersistence.springdatajpa.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FindUsersUsingQueriesTest
        extends SpringDataJpaApplicationTests {
    @Test
    void testFindAll() {
        List<User> users = userRepository.findAll();
        assertEquals(10, users.size());
    }

    @Test
    void testFindUser() {
        User beth = userRepository.findByUsername("beth");
        assertEquals("beth", beth.getUsername());
    }

    @Test
    void testFindAllOrderByUsernameDesc() {
        List<User> usersByUsernameDesc = userRepository.findAllByOrderByUsernameDesc();
        assertEquals(10, usersByUsernameDesc.size());
        assertEquals("stephanie", usersByUsernameDesc.get(0).getUsername());
        assertEquals("beth", usersByUsernameDesc.get(usersByUsernameDesc.size() - 1).getUsername());
    }

    @Test
    void testFindByRegistrationDateBetween() {
        List<User> users = userRepository.findByRegistrationDateBetween(LocalDate.of(2020, Month.JULY, 1),
                LocalDate.of(2020, Month.DECEMBER, 31));
        assertEquals(4, users.size());
    }

    @Test
    void testFindByUsernameEmail() {
        List<User> usersNameAndEmailNotExist = userRepository.findByUsernameAndEmail("stephanie",
                "beth@somedomain.com");
        List<User> usersNameAndEmailExist = userRepository.findByUsernameAndEmail("beth", "beth@somedomain.com");
        List<User> userList3 = userRepository.findByUsernameOrEmail("beth", "stephanie@someotherdomain.com");
        List<User> userList4 = userRepository.findByUsernameOrEmail("beth", "phuong@somedomain.com");
        assertEquals(0, usersNameAndEmailNotExist.size());
        assertEquals(1, usersNameAndEmailExist.size());
        assertEquals(2, userList3.size());
        assertEquals(1, userList4.size());
    }

    @Test
    void testFindByUsernameIgnoreCase() {
        List<User> users = userRepository.findByUsernameIgnoreCase("MIKE");
        assertAll(
                () -> assertEquals(1, users.size()),
                () -> assertEquals("mike", users.get(0).getUsername()));
    }

    @Test
    void testFindLevelOrderByUsernameDesc() {
        List<User> users = userRepository.findByLevelOrderByUsernameDesc(3);
        assertAll(
                () -> assertEquals(2, users.size()),
                () -> assertEquals("mike", users.get(0).getUsername()),
                () -> assertEquals("james", users.get(1).getUsername()));
    }

    @Test
    void testFindLevelGreaterThanEqual() {
        List<User> users = userRepository.findByLevelGreaterThanEqual(3);
        assertEquals(5, users.size());
    }

    @Test
    void testFindByUsername() {
        List<User> usersContaining = userRepository.findByUsernameContaining("ar");
        List<User> usersLike = userRepository.findByUsernameLike("%ar%");
        List<User> usersStarting = userRepository.findByUsernameStartingWith("b");
        List<User> usersEnding = userRepository.findByUsernameEndingWith("ie");

        assertAll(
                () -> assertEquals(2, usersContaining.size()),
                () -> assertEquals(2, usersLike.size()),
                () -> assertEquals(2, usersStarting.size()),
                () -> assertEquals(2, usersEnding.size()));
    }

    @Test
    void testFindByActive() {
        List<User> usersActive = userRepository.findByActive(true);
        List<User> usersNotActive = userRepository.findByActive(false);

        assertAll(
                () -> assertEquals(8, usersActive.size()),
                () -> assertEquals(2, usersNotActive.size()));
    }

    @Test
    void testFindByRegistrationDateInNotIn() {
        LocalDate date1 = LocalDate.of(2020, Month.JANUARY, 18);
        LocalDate date2 = LocalDate.of(2021, Month.JANUARY, 5);

        List<LocalDate> dates = new ArrayList<>();
        dates.add(date1);
        dates.add(date2);

        List<User> usersList1 = userRepository.findByRegistrationDateIn(dates);
        List<User> usersList2 = userRepository.findByRegistrationDateNotIn(dates);

        assertAll(
                () -> assertEquals(3, usersList1.size()),
                () -> assertEquals(7, usersList2.size()));
    }
}
