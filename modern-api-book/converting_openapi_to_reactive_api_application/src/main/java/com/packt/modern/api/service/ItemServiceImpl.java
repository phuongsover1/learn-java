package com.packt.modern.api.service;

import com.packt.modern.api.entity.ItemEntity;
import com.packt.modern.api.entity.ProductEntity;
import com.packt.modern.api.model.Item;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ItemServiceImpl implements ItemService {
  @Override
  public ItemEntity toEntity(Item s) {
    return new ItemEntity()
        .setQuantity(s.getQuantity())
        .setPrice(s.getUnitPrice())
        .setProduct(new ProductEntity().setId(UUID.fromString(s.getId())));
  }

  @Override
  public List<ItemEntity> toEntityList(List<Item> items) {
    if (Objects.isNull(items))
      return List.of();
    return items.stream().map(this::toEntity).toList();
  }

  @Override
  public Item toModel(ItemEntity e) {
    return new Item().id(e.getProduct().getId().toString())
        .unitPrice(e.getProduct().getPrice())
        .quantity(e.getQuantity());
  }

  @Override
  public List<Item> toModelList(List<ItemEntity> items) {
    if (Objects.isNull(items))
      return List.of();
    return items.stream().map(this::toModel).toList();
  }
}
