package com.packt.modern.api.entity;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table("ecomm.product")
public class ProductEntity {

  @Id
  @Column("id")
  private UUID id;

  @NotNull(message = "Product name is required.")
  @Column("name")
  private String name;

  @Column("description")
  private String description;

  @Column("price")
  private BigDecimal price;

  @Column("count")
  private int count;

  @Column("image_url")
  private String imageUrl;

  @Transient
  private List<TagEntity> tags = new ArrayList<>();

  private List<ItemEntity> items;

  public ProductEntity(UUID id, @NotNull(message = "Product name is required.") String name,
                       String description, BigDecimal price, int count, String imageUrl) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.description = description;
    this.count = count;
    this.imageUrl = imageUrl;
  }

  public ProductEntity() {
  }

  public UUID getId() {
    return id;
  }

  public ProductEntity setId(UUID id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public ProductEntity setName(String name) {
    this.name = name;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public ProductEntity setDescription(String description) {
    this.description = description;
    return this;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public ProductEntity setPrice(BigDecimal price) {
    this.price = price;
    return this;
  }

  public int getCount() {
    return count;
  }

  public ProductEntity setCount(int count) {
    this.count = count;
    return this;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public ProductEntity setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
    return this;
  }

  public List<TagEntity> getTags() {
    return tags;
  }

  public ProductEntity setTags(List<TagEntity> tags) {
    this.tags = tags;
    return this;
  }

  public List<ItemEntity> getItem() {
    return items;
  }

  public ProductEntity setItem(List<ItemEntity> item) {
    this.items = item;
    return this;
  }
}
