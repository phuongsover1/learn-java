package com.manning.persistence.ch07.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.math.BigDecimal;

@Embeddable
@AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "DIMENSIONS_NAME")),
        @AttributeOverride(name = "symbol", column = @Column(name = "DIMENSIONS_SYMBOL"))
})
public class Dimension extends Measurement {
    @NotNull
    private BigDecimal depth;
    @NotNull
    private BigDecimal height;
    @NotNull
    private BigDecimal width;

    public Dimension(String name, String symbol, BigDecimal depth, BigDecimal height, BigDecimal width) {
        super(name, symbol);
        this.depth = depth;
        this.height = height;
        this.width = width;
    }

    public Dimension() {
        super();
    }
}
