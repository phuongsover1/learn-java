package com.manning.persistence.ch07.model;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public abstract class Measurement {
    @NotNull
    private String name;

    @NotNull
    private String symbol;

    public Measurement(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public Measurement() {

    }
}
