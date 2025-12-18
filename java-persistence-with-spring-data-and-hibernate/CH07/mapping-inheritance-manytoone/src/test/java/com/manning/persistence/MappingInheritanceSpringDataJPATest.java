package com.manning.persistence;

import com.manning.persistence.ch07.model.BankAccount;
import com.manning.persistence.ch07.model.CreditCard;
import com.manning.persistence.ch07.model.User;
import com.manning.persistence.ch07.repositories.BankAccountRepository;
import com.manning.persistence.ch07.repositories.CreditCardRepository;
import com.manning.persistence.ch07.repositories.UserRepository;
import com.manning.persistence.configuration.SpringDataConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDataConfiguration.class})
public class MappingInheritanceSpringDataJPATest {
    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void storeLoadEntities() {
        CreditCard creditCard = new CreditCard(
                "John Smith", "123456789", "10", "2030"
        );
        creditCardRepository.save(creditCard);

        BankAccount bankAccount = new BankAccount(
                "Mike Johnson", "12345", "Delta Bank", "BANKXY12"
        );
        bankAccountRepository.save(bankAccount);

        List<CreditCard> creditCards = creditCardRepository.findByOwner("John Smith");
        List<BankAccount> bankAccounts = bankAccountRepository.findByOwner("Mike Johnson");

        List<CreditCard> creditCards1 = creditCardRepository.findByExpYear("2030");
        List<BankAccount> bankAccounts1 = bankAccountRepository.findBySwift("BANKXY12");

        assertAll(
                () -> assertEquals(1, creditCards.size()),
                () -> assertEquals("123456789", creditCards.get(0).getCardNumber()),
                () -> assertEquals(1, bankAccounts.size()),
                () -> assertEquals("12345", bankAccounts.get(0).getAccount()),
                () -> assertEquals(1, creditCards1.size()),
                () -> assertEquals("John Smith", creditCards1.get(0).getOwner()),
                () -> assertEquals(1, bankAccounts1.size()),
                () -> assertEquals("Mike Johnson", bankAccounts1.get(0).getOwner())
        );
    }

    @Test
    public void testManyToOne_DefaultBilling() {
        CreditCard creditCard = new CreditCard("John Smith", "123456789", "10", "2030");
        User john = new User("John Smith");
        john.setDefaultBilling(creditCard);
        creditCardRepository.save(creditCard);
        userRepository.save(john);

        List<User> users = userRepository.findAll();
        users.get(0).getDefaultBilling().pay(123);
    }

}
