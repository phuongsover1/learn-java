package com.packt.modern.api.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.packt.modern.api.entity.CartEntity;
import com.packt.modern.api.entity.ItemEntity;
import com.packt.modern.api.model.Item;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ItemServiceImpl implements ItemService {

  @Override
  public Mono<List<Item>> fluxToList(Flux<ItemEntity> items) {
    if (Objects.isNull(items))
      return Mono.just(Collections.emptyList());
    return items.map(e -> toModel(e)).collectList();
  }

  @Override
  public Mono<ItemEntity> toEntity(Mono<Item> item) {
    return item.map(i -> new ItemEntity()
    .setQuantity(i.getQuantity())
    .setPrice(i.getUnitPrice()));
  }


  @Override
  public Flux<Item> toItemFlux(Mono<CartEntity> cart) {
    if (Objects.isNull(cart))
      return Flux.empty();

    return cart.flatMapMany(c -> toModelFlux(c.getItems()));
  }

  @Override
  public Flux<Item> toModelFlux(List<ItemEntity> items) {
    if (Objects.isNull(items))
      return Flux.empty();

    return Flux.fromIterable(items.stream().map(i -> toModel(i)).toList());
  }


  @Override
  public Item toModel(ItemEntity e) {
      Item item = new Item();
      item.setId(e.getProductId().toString());
      item.setQuantity(e.getQuantity());
      item.setUnitPrice(e.getPrice());
      return item;
  }

  @Override
  public List<Item> toModelList(List<ItemEntity> items) {
      if (Objects.isNull(items))
        return Collections.emptyList();
      return items.stream().map(this::toModel).toList();
  }

  @Override
  public List<ItemEntity> toEntityList(List<Item> items) {
     if (Objects.isNull(items)) 
      return Collections.emptyList();
    return items.stream().map(this::toEntity).toList();
  }

  @Override
  public ItemEntity toEntity(Item source) {
      ItemEntity item  =  new ItemEntity();
      BeanUtils.copyProperties(source, item);
      item.setId(null);
      item.setProductId(UUID.fromString(source.getId()));
      item.setPrice(source.getUnitPrice());
      return item;
  }

  
}
