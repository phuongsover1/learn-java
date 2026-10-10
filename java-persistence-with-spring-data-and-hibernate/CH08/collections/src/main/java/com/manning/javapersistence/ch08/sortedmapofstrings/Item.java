package com.manning.javapersistence.ch08.sortedmapofstrings;

import java.time.LocalDate;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.SortComparator;
import org.hibernate.annotations.SortNatural;

import com.manning.javapersistence.ch08.Constants;

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
    @MapKeyColumn(name = "FILENAME") // key
    @Column(name = "IMAGENAME") // value
    @SortComparator(ReverseStringComparator.class)
    // @SortNatural // 
    private SortedMap<String, String> images = new TreeMap<>();

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

    public SortedMap<String, String> getImages() {
        return images;
    }

    public void setImages(SortedMap<String, String> images) {
        this.images = images;
    }
}
