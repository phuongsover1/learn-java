package com.manning.javapersistence.ch12.proxy;

import com.manning.javapersistence.ch12.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "Bid")
public class BidForEntityGraph {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private ItemForEntityGraph item;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User bidder;

    @NotNull
    private BigDecimal amount;

    public BidForEntityGraph() {
    }

    public BidForEntityGraph(ItemForEntityGraph item, User bidder, BigDecimal amount) {
        this.item = item;
        this.bidder = bidder;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public ItemForEntityGraph getItem() {
        return item;
    }

    public void setItem(ItemForEntityGraph item) {
        this.item = item;
    }

    public User getBidder() {
        return bidder;
    }

    public void setBidder(User bidder) {
        this.bidder = bidder;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
