package com.manning.javapersistence.ch12.proxy;

import com.manning.javapersistence.ch12.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class ImageX {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    @NotNull
    private String fileName;

    public ImageX() {
    }

    public ImageX(Item item, String fileName) {
        this.item = item;
        this.fileName = fileName;
    }

    public Long getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
