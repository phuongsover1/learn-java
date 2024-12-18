package com.packt.modern.api.controllers;

import com.packt.modern.api.PaymentApi;
import com.packt.modern.api.hateoas.PaymentRepresentationModelAssembler;
import com.packt.modern.api.model.Authorization;
import com.packt.modern.api.model.PaymentReq;
import com.packt.modern.api.service.PaymentService;

import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

@RestController
public class PaymentController implements PaymentApi {
  private final PaymentService pService;
  private final PaymentRepresentationModelAssembler assembler;

  public PaymentController(PaymentService pService, PaymentRepresentationModelAssembler assembler) {
    this.pService = pService;
    this.assembler = assembler;
  }

  @Override
  public Mono<ResponseEntity<Authorization>> authorize(Mono<PaymentReq> paymentReq, ServerWebExchange exchange)  {
    return null;
  }

  @Override
  public Mono<ResponseEntity<Authorization>> getOrdersPaymentAuthorization(String orderId, ServerWebExchange exchange)  {
    return null;
  }
}
