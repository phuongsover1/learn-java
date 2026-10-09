package com.manning.javapersistence.ch08.bagofstring;

import com.manning.javapersistence.ch08.Constants;

import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Item {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private LocalDate auctionEnd;

    @ElementCollection
    @CollectionTable(name = "IMAGE", joinColumns = @JoinColumn(name = "ITEM_ID"))
    @Column(name = "FILENAME")
    @GenericGenerator(name = "sequence_gen", strategy = "sequence")
    @CollectionId(
            column = @Column(name = "IMAGE_ID"),
            type = @Type(type = "long"),
            generator = "sequence_gen")
    private Collection<String> images = new ArrayList<>();

    public Item() {
    }

    public Item(String name, LocalDate auctionEnd) {
        this.name = name;
        this.auctionEnd = auctionEnd;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getAuctionEnd() {
        return auctionEnd;
    }

    public void setAuctionEnd(LocalDate auctionEnd) {
        this.auctionEnd = auctionEnd;
    }

    public Collection<String> getImages() {
        return images;
    }

    public void setImages(Collection<String> images) {
        this.images = images;
    }
}
