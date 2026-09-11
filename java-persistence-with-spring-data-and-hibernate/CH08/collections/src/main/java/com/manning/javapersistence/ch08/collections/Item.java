package com.manning.javapersistence.ch08.collections;

import com.manning.javapersistence.ch08.Constants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Item {

    private Long id;

    private String name;

    private LocalDate auctionEnd;

    public Item() {
    }

    public Item(String name, LocalDate auctionEnd) {
        this.name = name;
        this.auctionEnd = auctionEnd;
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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    public LocalDate getAuctionEnd() {
        return auctionEnd;
    }

    public void setAuctionEnd(LocalDate auctionEnd) {
        this.auctionEnd = auctionEnd;
    }
}
