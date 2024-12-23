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
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((productId == null) ? 0 : productId.hashCode());
    result = prime * result + ((price == null) ? 0 : price.hashCode());
    result = prime * result + quantity;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ItemEntity other = (ItemEntity) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (productId == null) {
      if (other.productId != null)
        return false;
    } else if (!productId.equals(other.productId))
      return false;
    if (price == null) {
      if (other.price != null)
        return false;
    } else if (!price.equals(other.price))
      return false;
    if (quantity != other.quantity)
      return false;
    return true;
  }


}
