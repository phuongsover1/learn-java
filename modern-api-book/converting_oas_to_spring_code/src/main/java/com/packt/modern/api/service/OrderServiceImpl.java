package com.packt.modern.api.service;

import com.packt.modern.api.entity.OrderEntity;
import com.packt.modern.api.exceptions.ResourceNotFoundException;
import com.packt.modern.api.model.NewOrder;
import com.packt.modern.api.repository.OrderRepository;
import com.packt.modern.api.repository.UserRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

  private final OrderRepository oRepo;
  private final UserRepository uRepo;

  public OrderServiceImpl(OrderRepository oRepo, UserRepository uRepo) {
    this.oRepo = oRepo;
    this.uRepo = uRepo;
  }

  @Override
  public Optional<OrderEntity> addOrder(NewOrder newOrder) {
    if (Strings.isEmpty(newOrder.getCustomerId())) {
      throw new ResourceNotFoundException("Invalid customer id.");
    }
    if (Objects.isNull(newOrder.getAddress()) || Strings.isEmpty(newOrder.getAddress().getId())) {
      throw new ResourceNotFoundException("Invalid address id.");
    }
    if (Objects.isNull(newOrder.getCard()) || Strings.isEmpty(newOrder.getCard().getId())) {
      throw new ResourceNotFoundException("Invalid card id.");
    }

    // 1. Save Order
    return oRepo.insert(newOrder);
    // Ideally, here it will trigger the rest of the process
    // 2. Initiate the payment
    // 3. Once the payment is authorized, change the status to paid
    // 4. Initiate the Shipment and changed the status to Shipment Initiated or Shipped
  }

  @Override
  public Iterable<OrderEntity> getOrdersByCustomerId(String customerId) {
    return oRepo.findByCustomerId(UUID.fromString(customerId));
  }

  @Override
  public Optional<OrderEntity> getByOrderId(String id) {
    return oRepo.findById(UUID.fromString(id));
  }
}
