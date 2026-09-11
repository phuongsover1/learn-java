package com.manning.javapersistence.ch08.collections;

import com.manning.javapersistence.ch08.Constants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class Bid {

    private Long id;

    private BigDecimal amount;

    public Bid() {
    }

    public Bid(BigDecimal amount) {
        this.amount = amount;
    }

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
