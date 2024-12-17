package com.packt.modern.api.service;

import com.packt.modern.api.entity.CartEntity;
import com.packt.modern.api.entity.ItemEntity;
import com.packt.modern.api.entity.UserEntity;
import com.packt.modern.api.exceptions.CustomerNotFoundException;
import com.packt.modern.api.exceptions.GenericAlreadyExistsException;
import com.packt.modern.api.exceptions.ItemNotFoundException;
import com.packt.modern.api.exceptions.ResourceNotFoundException;
import com.packt.modern.api.model.Item;
import com.packt.modern.api.repository.CartRepository;
import com.packt.modern.api.repository.ItemRepository;
import com.packt.modern.api.repository.UserRepository;
import org.springframework.objenesis.instantiator.util.UnsafeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

@Service
public class CartServiceImpl implements CartService {
  private final CartRepository cRepo;
  private final UserRepository uRepo;
  private final ItemService iService;
  private final ItemRepository iRepo;
  private final BiFunction<CartEntity, ItemEntity, CartEntity> cartEntityBiFun = (c, i) -> {
    c.getItems().add(i);
    return c;
  };

  private final BiFunction<CartEntity, List<ItemEntity>, CartEntity> cartItemBiFun = (c, i) -> c.setItems(i);

  private final BiFunction<CartEntity, UserEntity, CartEntity> cartUserBiFun = (c, u) -> c.setUser(u);

  public CartServiceImpl(CartRepository cRepo, UserRepository uRepo, ItemService iService, ItemRepository iRepo) {
    this.cRepo = cRepo;
    this.uRepo = uRepo;
    this.iService = iService;
    this.iRepo = iRepo;
  }

  @Override
  @Transactional
  public Flux<Item> addCartItemsByCustomerId(CartEntity cartEntity, Mono<Item> newItem) {
    final List<ItemEntity> cartItems = cartEntity.getItems();
    return newItem.flatMap(ni -> {
      long countExisting = cartItems.stream()
          .filter(i -> i.getProductId().equals(UUID.fromString(ni.getId()))).count();
      if (countExisting == 1) {
        return Mono.error(new GenericAlreadyExistsException(String.format(
            "Requested Item (%s) is alredy there in cart. Please make a PUT call for update.", ni.getId()
        )));
      }

      return iRepo.save(iService.toEntity(ni)).flatMap(i -> iRepo.saveMapping(cartEntity.getId(), i.getId()).then(
              getUpdatedList(cartItems, i)
          )
      );
    }).flatMapMany(Flux::fromIterable);
  }

  private Mono<List<Item>> getUpdatedList(List<ItemEntity> cartItems, ItemEntity savedItem) {
    cartItems.add(savedItem);
    return Mono.just(iService.toModelList(cartItems));
  }

  @Override
  public Flux<Item> addOrReplaceItemsByCustomerId(CartEntity cartEntity, Mono<Item> newItem) {
    final List<ItemEntity> cartItems = cartEntity.getItems();
    return newItem.flatMap(ni -> {
      List<ItemEntity> existing = cartItems.stream().filter(i -> i.getProductId().equals(UUID.fromString(ni.getId()))).toList();
      if (existing.size() == 1) {
        existing.get(0).setPrice(ni.getUnitPrice()).setQuantity(ni.getQuantity());
        return iRepo.save(existing.get(0)).flatMap(i -> getUpdatedList(cartItems.stream().filter(j -> !j.getProductId().equals(UUID.fromString(ni.getId())))
            .toList(), i));
      }

      return iRepo.save(iService.toEntity(ni)).flatMap(i -> iRepo.saveMapping(cartEntity.getId(), i.getId()).then(
          getUpdatedList(cartItems, i)
      ));
    }).flatMapMany(Flux::fromIterable);
  }

  @Override
  @Transactional
  public Mono<Void> deleteCart(String customerId, String cartId) {
    Mono<List<UUID>> monoIds = iRepo.findByCustomerId(UUID.fromString(customerId))
        .switchIfEmpty(Mono.error(new ResourceNotFoundException(
            ". No items found in Cart of customer Id - " + customerId
        )))
        .map(i -> i.getId())
        .collectList().cache();

    return monoIds.zipWhen(l -> {
      List <UUID> ids = l.subList(0, l.size());
      return iRepo.deleteCartitemJoinByid(ids, UUID.fromString(cartId))
      .then(iRepo.deleteByIds(ids).subscribeOn(Schedulers.boundedElastic()));
    }).then();
  }


  @Override
  public Mono<Void> deleteItemFromCart(CartEntity cartEntity, String itemId) {
     List<ItemEntity> items = cartEntity.getItems(); 
     items = items.stream()
     .filter(i -> i.getId().equals(UUID.fromString(itemId))).toList();
     if (items.size() != 1) {
      return Mono.error(new ResourceNotFoundException(
        ". No items found in Cart with Id - " + itemId
      ));
     }
     List<UUID> ids = items.stream().map(i -> i.getId()).toList();
     return iRepo.deleteCartitemJoinByid(ids, cartEntity.getId())
     .then(iRepo.deleteByIds(ids).subscribeOn(Schedulers.boundedElastic()));
  }

  @Override
  public Mono<CartEntity> getCartByCustomerId(String customerId) {
      Mono<CartEntity> cart = cRepo.findByCustomerId(UUID.fromString(customerId))
        .subscribeOn(Schedulers.boundedElastic());
      Mono<UserEntity> user = uRepo.findById(UUID.fromString(customerId))
        .subscribeOn(Schedulers.boundedElastic());
      cart = Mono.zip(cart, user, cartUserBiFun);

      Flux<ItemEntity> items = iRepo.findByCustomerId(UUID.fromString(customerId));
      return Mono.zip(cart, items.collectList(), cartItemBiFun);
  }

}
