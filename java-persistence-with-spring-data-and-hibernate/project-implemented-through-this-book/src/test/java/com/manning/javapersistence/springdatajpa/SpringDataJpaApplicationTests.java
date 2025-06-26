package com.manning.javapersistence.springdatajpa;

import com.github.javafaker.Faker;
import com.manning.javapersistence.springdatajpa.model.Address;
import com.manning.javapersistence.springdatajpa.model.City;
import com.manning.javapersistence.springdatajpa.model.GermanZipcode;
import com.manning.javapersistence.springdatajpa.model.SwissZipcode;
import com.manning.javapersistence.springdatajpa.model.User;
import com.manning.javapersistence.springdatajpa.repositories.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class SpringDataJpaApplicationTests {
        @Autowired
        UserRepository userRepository;

        @BeforeAll
        void beforeAll() {
                userRepository.saveAll(generaUsers());
        }

        private static List<User> generaUsers() {
                Faker faker = new Faker();
                List<User> users = new ArrayList<>();

                User john = new User("john", LocalDate.of(2020, Month.APRIL, 13));
                john.setEmail("john@somedomain.com");
                john.setLevel(1);
                john.setActive(true);
                john.setHomeAddress(new Address(faker.address().streetAddress(),
                                new City(new GermanZipcode("12345"), faker.country().name(),
                                                faker.address().cityName())));

                User mike = new User("mike", LocalDate.of(2020, Month.JANUARY, 18));
                mike.setEmail("mike@somedomain.com");
                mike.setLevel(3);
                mike.setActive(true);
                mike.setHomeAddress(new Address(faker.address().streetAddress(),
                                new City(new SwissZipcode("1234"), faker.country().name(),
                                                faker.address().cityName())));

                // User james = new User("james", LocalDate.of(2020, Month.MARCH, 11));
                // james.setEmail("james@someotherdomain.com");
                // james.setLevel(3);
                // james.setActive(false);
                // james.setHomeAddress(new Address(faker.address().streetAddress(),
                // new City(new SwissZipcode("123456"), faker.country().name(),
                // faker.address().cityName())));

                User katie = new User("katie", LocalDate.of(2021, Month.JANUARY, 5));
                katie.setEmail("katie@somedomain.com");
                katie.setLevel(5);
                katie.setActive(true);
                katie.setHomeAddress(new Address(faker.address().streetAddress(),
                                new City(new SwissZipcode("1234"), faker.country().name(),
                                                faker.address().cityName())));

                User beth = new User("beth", LocalDate.of(2020, Month.AUGUST, 3));
                beth.setEmail("beth@somedomain.com");
                beth.setLevel(2);
                beth.setActive(true);
                beth.setHomeAddress(new Address(faker.address().streetAddress(),
                                new City(new SwissZipcode("1234"), faker.country().name(),
                                                faker.address().cityName())));

                User julius = new User("julius", LocalDate.of(2021, Month.FEBRUARY, 9));
                julius.setEmail("julius@someotherdomain.com");
                julius.setLevel(4);
                julius.setActive(true);
                julius.setHomeAddress(new Address(faker.address().streetAddress(),
                                new City(new SwissZipcode("1234"), faker.country().name(),
                                                faker.address().cityName())));

                User darren = new User("darren", LocalDate.of(2020, Month.DECEMBER, 11));
                darren.setEmail("darren@somedomain.com");
                darren.setLevel(2);
                darren.setActive(true);
                darren.setHomeAddress(new Address(faker.address().streetAddress(),
                                new City(new GermanZipcode("12345"), faker.country().name(),
                                                faker.address().cityName())));

                User marion = new User("marion", LocalDate.of(2020, Month.SEPTEMBER, 23));
                marion.setEmail("marion@someotherdomain.com");
                marion.setLevel(2);
                marion.setActive(false);
                marion.setHomeAddress(new Address(faker.address().streetAddress(),
                                new City(new GermanZipcode("12345"), faker.country().name(),
                                                faker.address().cityName())));

                User stephanie = new User("stephanie", LocalDate.of(2020, Month.JANUARY, 18));
                stephanie.setEmail("stephanie@someotherdomain.com");
                stephanie.setLevel(4);
                stephanie.setActive(true);
                stephanie.setHomeAddress(new Address(faker.address().streetAddress(),
                                new City(new GermanZipcode("12345"), faker.country().name(),
                                                faker.address().cityName())));

                User burk = new User("burk", LocalDate.of(2020, Month.NOVEMBER, 28));
                burk.setEmail("burk@somedomain.com");
                burk.setLevel(1);
                burk.setActive(true);
                burk.setHomeAddress(new Address(faker.address().streetAddress(),
                                new City(new GermanZipcode("12345"), faker.country().name(),
                                                faker.address().cityName())));

                users.add(john);
                users.add(mike);
                // users.add(james);
                users.add(katie);
                users.add(beth);
                users.add(julius);
                users.add(darren);
                users.add(marion);
                users.add(stephanie);
                users.add(burk);

                return users;
        }

        // @AfterAll
        // void afterAll() {
        // userRepository.deleteAll();
        // }
}
