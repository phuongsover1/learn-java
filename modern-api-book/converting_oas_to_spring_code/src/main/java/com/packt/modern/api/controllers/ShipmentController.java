package com.packt.modern.api.controllers;

import com.packt.modern.api.ShipmentApi;
import com.packt.modern.api.hateoas.ShipmentRepresentationAssembler;
import com.packt.modern.api.model.Shipment;
import com.packt.modern.api.service.OrderService;
import com.packt.modern.api.service.ShipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.notFound;

@RestController
public class ShipmentController implements ShipmentApi {
  private final ShipmentService sService;
  private final ShipmentRepresentationAssembler assembler;

  public ShipmentController(ShipmentService sService, ShipmentRepresentationAssembler assembler) {
    this.sService = sService;
    this.assembler = assembler;
  }

  @Override
  public ResponseEntity<Shipment> getShipmentByOrderId(String id) {
    return sService.getShipmentByOrderId(id).map(assembler::toModel).map(ResponseEntity::ok).orElse(notFound().build());
  }
}
