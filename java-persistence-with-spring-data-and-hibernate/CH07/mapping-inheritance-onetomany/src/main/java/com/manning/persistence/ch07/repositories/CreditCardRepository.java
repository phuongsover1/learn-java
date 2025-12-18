package com.manning.persistence.ch07.repositories;

import com.manning.persistence.ch07.model.CreditCard;

import java.util.List;

public interface CreditCardRepository extends BillingDetailsRepository<CreditCard, Long> {
    List<CreditCard> findByExpYear(String expYear);
}
