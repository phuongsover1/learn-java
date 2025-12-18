package com.manning.persistence.ch07.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;
    @ManyToOne
    private BillingDetails defaultBilling;

    @NotNull
    private String name;

    public User(String name) {
        this.name = name;
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BillingDetails getDefaultBilling() {
        return defaultBilling;
    }

    public void setDefaultBilling(BillingDetails defaultBilling) {
        this.defaultBilling = defaultBilling;
    }
}
