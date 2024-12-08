package com.packt.modern.api.service;

import com.packt.modern.api.entity.ItemEntity;
import com.packt.modern.api.exceptions.ItemNotFoundException;
import com.packt.modern.api.model.Item;
import com.packt.modern.api.entity.CartEntity;
import com.packt.modern.api.exceptions.CustomerNotFoundException;
import com.packt.modern.api.exceptions.GenericAlreadyExistsException;
import com.packt.modern.api.repository.CartRepository;
import com.packt.modern.api.repository.UserRepository;
import org.springframework.objenesis.instantiator.util.UnsafeUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CartServiceImpl implements CartService {
  private final CartRepository cRepo;
  private final UserRepository uRepo;
  private final ItemService iService;

  public CartServiceImpl(CartRepository cRepo, UserRepository uRepo, ItemService iService) {
    this.cRepo = cRepo;
    this.uRepo = uRepo;
    this.iService = iService;
  }

  @Override
  public List<Item> addCartItemsByCustomerId(String customerId, Item item) {
    CartEntity cartEntity = getCartByCustomerId(customerId);
    long count = cartEntity.getItems().stream()
        .filter(i -> i.getProduct().getId()
            .equals(UUID.fromString(item.getId())))
        .count();
    if (count > 0) {
      throw new GenericAlreadyExistsException(
          String.format("Item with Id (%s) already exist. You can update it.", item.getId())
      );
    }
    return List.of(new Item());
  }

  @Override
  public List<Item> addOrReplaceItemsByCustomerId(String customerId, Item item) {
    return List.of();
  }

  @Override
  public void deleteCart(String customerId) {
    CartEntity cartEntity = getCartByCustomerId(customerId);
    cRepo.deleteById(cartEntity.getId());
  }

  @Override
  public void deleteItemFromCart(String customerId, String itemId) {

  }

  @Override
  public CartEntity getCartByCustomerId(String customerId) {
    CartEntity cartEntity = cRepo.findByCustomerId(UUID.fromString(customerId))
        .orElse(new CartEntity());
    if (Objects.isNull(cartEntity.getUser())) {
      cartEntity.setUser(uRepo.findById(UUID.fromString(customerId)).orElseThrow(
          () -> new CustomerNotFoundException(String.format(" - %s", customerId))
      ));
    }
    return cartEntity;
  }

  @Override
  public List<Item> getCartItemsByCustomerId(String customerId) {
    CartEntity cartEntity = getCartByCustomerId(customerId);
    return iService.toModelList(cartEntity.getItems());
  }

  @Override
  public Item getCartItemsByItemId(String customerId, String itemId) {
    CartEntity cartEntity = getCartByCustomerId(customerId);
    AtomicReference<ItemEntity> itemEntity = new AtomicReference<>();
    cartEntity.getItems().stream().forEach(i -> {
      if (i.getId().toString().equals(itemId))
        itemEntity.set(i);
    });
    if (Objects.isNull(itemEntity.get())) {
      UnsafeUtils.getUnsafe().throwException(new ItemNotFoundException(String.format(" - %s", itemId)));
    }
    return iService.toModel(itemEntity.get());
  }
}
