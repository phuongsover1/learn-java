package com.manning.javapersistence.springdatajpa;

import com.github.javafaker.Faker;
import com.manning.javapersistence.springdatajpa.configuration.SpringDataConfiguration;
import com.manning.javapersistence.springdatajpa.model.Address;
import com.manning.javapersistence.springdatajpa.model.AutionType;
import com.manning.javapersistence.springdatajpa.model.City;
import com.manning.javapersistence.springdatajpa.model.GermanZipcode;
import com.manning.javapersistence.springdatajpa.model.Item;
import com.manning.javapersistence.springdatajpa.model.MonetaryAmount;
import com.manning.javapersistence.springdatajpa.model.SwissZipcode;
import com.manning.javapersistence.springdatajpa.model.User;
import com.manning.javapersistence.springdatajpa.repositories.ItemRepository;
import com.manning.javapersistence.springdatajpa.repositories.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDataConfiguration.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class SpringDataJpaApplicationTests {
        @Autowired
        UserRepository userRepository;

        @Autowired
        ItemRepository itemRepository;

        @BeforeAll
        void beforeAll() {
                userRepository.saveAll(generaUsers());
                itemRepository.saveAll(generalItems());
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

                 User james = new User("james", LocalDate.of(2020, Month.MARCH, 11));
                 james.setEmail("james@someotherdomain.com");
                 james.setLevel(3);
                 james.setActive(false);
                 james.setHomeAddress(new Address(faker.address().streetAddress(),
                 new City(new SwissZipcode("1234"), faker.country().name(),
                 faker.address().cityName())));

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
                 users.add(james);
                users.add(katie);
                users.add(beth);
                users.add(julius);
                users.add(darren);
                users.add(marion);
                users.add(stephanie);
                users.add(burk);

                return users;
        }

        private static List<Item> generalItems() {
                List<Item> items = new ArrayList<>();
                Faker faker = new Faker();

                Item item1 = new Item();
                item1.setName(faker.book().title());
                item1.setAutionType(AutionType.FIXED_PRICE);
                item1.setDescription(faker.lorem().sentence());
                item1.setMetricWeight(faker.number().randomDouble(3, 0, 0));
                item1.setBuyNowPrice(new MonetaryAmount(new BigDecimal(13), Currency.getInstance(Locale.US)));

                Item item2 = new Item();
                item2.setName(faker.book().title());
                item2.setAutionType(AutionType.FIXED_PRICE);
                item2.setDescription(faker.lorem().sentence());
                item2.setMetricWeight(faker.number().randomDouble(3, 0, 0));
                item2.setBuyNowPrice(new MonetaryAmount(new BigDecimal(13), Currency.getInstance(Locale.US)));

                items.add(item1);
                items.add(item2);
                return items;
        }

        // @AfterAll
        // void afterAll() {
        // userRepository.deleteAll();
        // }
}
