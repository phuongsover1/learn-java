package com.manning.persistence;

import com.manning.persistence.ch07.model.BankAccount;
import com.manning.persistence.ch07.model.BillingDetails;
import com.manning.persistence.ch07.model.CreditCard;
import com.manning.persistence.ch07.model.User;
import com.manning.persistence.ch07.repositories.BankAccountRepository;
import com.manning.persistence.ch07.repositories.BillingDetailsRepository;
import com.manning.persistence.ch07.repositories.CreditCardRepository;
import com.manning.persistence.ch07.repositories.UserRepository;
import com.manning.persistence.configuration.SpringDataConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDataConfiguration.class})
public class MappingInheritanceSpringDataJPATest {
    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BillingDetailsRepository<BillingDetails, Long> billingDetailsRepository;


    @Test
    public void testManyToOne_DefaultBilling() {
        CreditCard creditCard = new CreditCard("123456789", "10", "2030");
        User john = new User("John Smith");
        creditCard.setOwner(john);
        userRepository.save(john);
        creditCardRepository.save(creditCard);

        List<User> users = userRepository.findAll();
        List<BillingDetails> billingDetails = billingDetailsRepository.findByOwnerName("John Smith");
        assertAll(
                () -> assertEquals(1, users.size()),
                () -> assertEquals(1, billingDetails.size())
        );
    }

}
