package com.packt.modern.api.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiFunction;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import com.packt.modern.api.entity.CardEntity;
import com.packt.modern.api.entity.ItemEntity;
import com.packt.modern.api.entity.OrderEntity;
import com.packt.modern.api.entity.ShipmentEntity;
import com.packt.modern.api.exceptions.ResourceNotFoundException;
import com.packt.modern.api.model.NewOrder;
import com.packt.modern.api.repository.AddressRepository;
import com.packt.modern.api.repository.CardRepository;
import com.packt.modern.api.repository.ItemRepository;
import com.packt.modern.api.repository.OrderRepository;
import com.packt.modern.api.repository.UserRepository;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderServiceImpl implements OrderService {

  private final OrderRepository oRepo;
  private final UserRepository uRepo;
  private final AddressRepository aRepo;
  private final CardRepository cRepo;
  private final ItemRepository iRepo;

  


public OrderServiceImpl(OrderRepository oRepo, UserRepository uRepo, AddressRepository aRepo, CardRepository cRepo,
        ItemRepository iRepo) {
    this.oRepo = oRepo;
    this.uRepo = uRepo;
    this.aRepo = aRepo;
    this.cRepo = cRepo;
    this.iRepo = iRepo;
}

@Override
  public Mono<OrderEntity> addOrder(Mono<NewOrder> newOrder) {
    return newOrder.filter(n -> {
      if (Strings.isEmpty(n.getCustomerId())) {
        throw new ResourceNotFoundException("Invalid customer id.");
      }
      if (Objects.isNull(n.getAddress()) || Strings.isEmpty(n.getAddress().getId())) {
        throw new ResourceNotFoundException("Invalid address id.");
      }
      if (Objects.isNull(n.getCard()) || Strings.isEmpty(n.getCard().getId())) {
        throw new ResourceNotFoundException("Invalid card id.");
      }
      return true;
    }).then(oRepo.insert(newOrder));
    // 1. Save Order
    // Ideally, here it will trigger the rest of the process
    // 2. Initiate the payment
    // 3. Once the payment is authorized, change the status to paid
    // 4. Initiate the Shipment and changed the status to Shipment Initiated or Shipped
  }
  private final BiFunction<OrderEntity, List<ItemEntity>, OrderEntity> orderItemsBiFunction = (o,il) -> {
    o.setItems(il);
    return o;
  };

  @Override
  public Flux<OrderEntity> getOrdersByCustomerId(String customerId) {
    return oRepo.findByCustomerId(UUID.fromString(customerId))
      .flatMap(order -> Mono.just(order)
      .zipWith(aRepo.findById(order.getAddressId()))
      .map(t -> t.getT1().setAddressEntity(t.getT2()))
      .zipWith(cRepo.findById(order.getCardId() != null ? order.getCardId() : UUID.fromString("0a59ba9f-629e-4445-8129-b9bce1985d6a"))
      .defaultIfEmpty(new CardEntity()))
      .map(t -> t.getT1().setCardEntity(t.getT2()))
      .zipWith(uRepo.findById(order.getCustomerId()))
      .map(t -> t.getT1().setUserEntity(t.getT2()))
      .zipWith(iRepo.findByCustomerId(order.getCustomerId()).collectList(), orderItemsBiFunction)
      );
  }

  @Override
  public Mono<OrderEntity> getByOrderId(String id) {
    return oRepo.findById(UUID.fromString(id))
      .flatMap(order -> Mono.just(order)
      .zipWith(aRepo.findById(order.getAddressId()))
      .map(t -> t.getT1().setAddressEntity(t.getT2()))
      .zipWith(cRepo.findById(order.getCardId() != null ? order.getCardId() : UUID.fromString("0a59ba9f-629e-4445-8129-b9bce1985d6a"))
      .defaultIfEmpty(new CardEntity()))
      .map(t -> t.getT1().setCardEntity(t.getT2()))
      .zipWith(uRepo.findById(order.getCustomerId()))
      .map(t -> t.getT1().setUserEntity(t.getT2()))
      .zipWith(iRepo.findByCustomerId(order.getCustomerId()).collectList(), orderItemsBiFunction)
      );
  }

  @Override
  public Mono<ShipmentEntity> getShipmentByOrderId(String id) {
    return Mono.empty();
  }

  @Override
  public Mono<OrderEntity> updateMapping(@Valid OrderEntity orderEntity) {
    return oRepo.updateMapping(orderEntity);
  }

}
