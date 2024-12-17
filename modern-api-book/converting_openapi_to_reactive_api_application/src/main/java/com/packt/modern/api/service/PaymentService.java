package com.packt.modern.api.service;

import com.packt.modern.api.entity.AuthorizationEntity;
import com.packt.modern.api.model.PaymentReq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Mono;

public interface PaymentService {

  Mono<AuthorizationEntity> authorize(@Valid PaymentReq paymentReq);
  Mono<AuthorizationEntity> getOrdersPaymentAuthorization(@NotNull String orderId);
}
