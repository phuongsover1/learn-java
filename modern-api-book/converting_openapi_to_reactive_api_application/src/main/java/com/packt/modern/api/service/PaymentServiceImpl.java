package com.packt.modern.api.service;

import com.packt.modern.api.entity.AuthorizationEntity;
import com.packt.modern.api.model.PaymentReq;
import com.packt.modern.api.repository.OrderRepository;
import com.packt.modern.api.repository.PaymentRepository;

import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
  private final PaymentRepository pRepo;
  private final OrderRepository oRepo;

  public PaymentServiceImpl(PaymentRepository pRepo, OrderRepository oRepo) {
    this.pRepo = pRepo;
    this.oRepo = oRepo;
  }

  @Override
  public Mono<AuthorizationEntity> authorize(PaymentReq paymentReq) {
    return Mono.empty();
  }

  @Override
  public Mono<AuthorizationEntity> getOrdersPaymentAuthorization(String orderId) {
    return oRepo.findById(UUID.fromString(orderId)).map(o -> o.getAuthorizationEntity());
  }
}
