package com.manning.persistence.ch07.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Embeddable
@AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "WEIGHT_NAME")),
        @AttributeOverride(name = "symbol", column = @Column(name = "WEIGHT_SYMBOL"))
})
public class Weight extends Measurement{
    @NotNull
    @Column(name = "WEIGHT")
    private BigDecimal value;

    public Weight(String name, String symbol, BigDecimal value) {
        super(name, symbol);
        this.value = value;
    }

    public Weight() {

    }
}
