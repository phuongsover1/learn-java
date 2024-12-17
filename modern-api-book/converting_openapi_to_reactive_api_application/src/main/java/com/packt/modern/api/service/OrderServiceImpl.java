package com.packt.modern.api.service;

import java.util.Objects;
import java.util.UUID;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import com.packt.modern.api.entity.OrderEntity;
import com.packt.modern.api.entity.ShipmentEntity;
import com.packt.modern.api.exceptions.ResourceNotFoundException;
import com.packt.modern.api.model.NewOrder;
import com.packt.modern.api.repository.AddressRepository;
import com.packt.modern.api.repository.CardRepository;
import com.packt.modern.api.repository.OrderRepository;
import com.packt.modern.api.repository.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderServiceImpl implements OrderService {

  private final OrderRepository oRepo;
  private final UserRepository uRepo;
  private final AddressRepository aRepo;
  private final CardRepository cRepo;

  


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

  @Override
  public Flux<OrderEntity> getOrdersByCustomerId(String customerId) {
    return oRepo.findByCustomerId(UUID.fromString(customerId))
    .zipWith(null)
  }

  @Override
  public Mono<OrderEntity> getByOrderId(String id) {
    return oRepo.findById(UUID.fromString(id));
  }

  @Override
  public Mono<ShipmentEntity> getShipmentByOrderId(String id) {
    return Mono.empty();
  }

  public OrderRepository getoRepo() {
    return oRepo;
  }

  public UserRepository getuRepo() {
    return uRepo;
  }

  public AddressRepository getaRepo() {
    return aRepo;
  }
}
