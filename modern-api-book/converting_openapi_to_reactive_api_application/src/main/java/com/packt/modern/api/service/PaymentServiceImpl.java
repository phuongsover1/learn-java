package com.packt.modern.api.service;

import com.packt.modern.api.entity.AuthorizationEntity;
import com.packt.modern.api.model.PaymentReq;
import com.packt.modern.api.repository.OrderRepository;
import com.packt.modern.api.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
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
  public Optional<AuthorizationEntity> authorize(PaymentReq paymentReq) {
    return Optional.empty();
  }

  @Override
  public Optional<AuthorizationEntity> getOrdersPaymentAuthorization(String orderId) {
    return oRepo.findById(UUID.fromString(orderId)).map(o -> o.getAuthorizationEntity());
  }
}
