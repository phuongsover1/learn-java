package com.manning.javapersistence.ch06.model;

import javax.persistence.*;

@Entity
public class Item {
  @Id
  @GeneratedValue(generator = "ID_GENERATOR")
  private Long id;

  @Access(AccessType.PROPERTY)
  @Column(name = "ITEM_NAME")
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = !name.startsWith("AUCTION: ") ? "AUCTION: " + name : name;
  }

}
