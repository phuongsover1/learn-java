package com.packt.modern.api.service;

import com.packt.modern.api.model.Item;
import com.packt.modern.api.entity.ItemEntity;

import java.util.List;

public interface ItemService {

  ItemEntity toEntity(Item s);

  List<ItemEntity> toEntityList(List<Item> items);

  Item toModel(ItemEntity e);

  List<Item> toModelList(List<ItemEntity> items);
}
