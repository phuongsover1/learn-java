package com.packt.modern.api.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Table(name = "ecomm.item")
public class ItemEntity {

  @Id
  @Column("id")
  private UUID id;

  @Column("product_id")
  private UUID productId;

  @Column("unit_price")
  private BigDecimal price;

  @Column("quantity")
  private int quantity;


  public UUID getProductId() {
    return productId;
  }

  public ItemEntity setProductId(UUID productId) {
    this.productId = productId;
    return this;
  }

  public UUID getId() {
    return id;
  }

  public ItemEntity setId(UUID id) {
    this.id = id;
    return this;
  }


  public BigDecimal getPrice() {
    return price;
  }

  public ItemEntity setPrice(BigDecimal price) {
    this.price = price;
    return this;
  }

  public int getQuantity() {
    return quantity;
  }

  public ItemEntity setQuantity(int quantity) {
    this.quantity = quantity;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ItemEntity that = (ItemEntity) o;
    return quantity == that.quantity && Objects.equals(id, that.id) && Objects.equals(productId, that.productId) && Objects.equals(price, that.price);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, productId, price, quantity);
  }
}
