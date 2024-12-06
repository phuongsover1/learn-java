package com.packt.modern.api.controllers;

import com.packt.modern.api.PaymentApi;
import com.packt.modern.api.hateoas.PaymentRepresentationAssembler;
import com.packt.modern.api.model.Authorization;
import com.packt.modern.api.model.PaymentReq;
import com.packt.modern.api.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController implements PaymentApi {
  private final PaymentService pService;
  private final PaymentRepresentationAssembler assembler;

  public PaymentController(PaymentService pService, PaymentRepresentationAssembler assembler) {
    this.pService = pService;
    this.assembler = assembler;
  }

  @Override
  public ResponseEntity<Authorization> authorize(PaymentReq paymentReq)  {
    return null;
  }

  @Override
  public ResponseEntity<Authorization> getOrdersPaymentAuthorization(String orderId)  {
    return null;
  }
}
