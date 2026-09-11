package com.manning.javapersistence.ch12.proxy;

import com.manning.javapersistence.ch12.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ImageX")
public class ImageXForEntityGraph {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private ItemForEntityGraph item;

    @NotNull
    private String fileName;

    public ImageXForEntityGraph() {
    }

    public ImageXForEntityGraph(ItemForEntityGraph item, String fileName) {
        this.item = item;
        this.fileName = fileName;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
