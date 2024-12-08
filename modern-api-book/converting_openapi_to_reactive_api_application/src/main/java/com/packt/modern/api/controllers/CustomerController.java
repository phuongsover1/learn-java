package com.packt.modern.api.controllers;

import com.packt.modern.api.CustomerApi;
import com.packt.modern.api.hateoas.AddressRepresentationModelAssembler;
import com.packt.modern.api.hateoas.CardRepresentationModelAssembler;
import com.packt.modern.api.hateoas.UserRepresentationModelAssembler;
import com.packt.modern.api.model.Address;
import com.packt.modern.api.model.Card;
import com.packt.modern.api.model.User;
import com.packt.modern.api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
public class CustomerController implements CustomerApi {
  private final UserService uService;
  private final UserRepresentationModelAssembler uAssembler;
  private final AddressRepresentationModelAssembler aAssembler;
  private final CardRepresentationModelAssembler cAssembler;

  public CustomerController(UserService uService, UserRepresentationModelAssembler assembler, AddressRepresentationModelAssembler aAssembler, CardRepresentationModelAssembler cAssembler) {
    this.uService = uService;
    this.uAssembler = assembler;
    this.aAssembler = aAssembler;
    this.cAssembler = cAssembler;
  }

  @Override
  public ResponseEntity<Void> deleteCustomerById(String id) {
    uService.deleteCustomerById(id);
    return accepted().build();
  }

  @Override
  public ResponseEntity<List<Address>> getAddressesByCustomerId(String id) {
    return uService.getAddressesByCustomerId(id).map(aAssembler::toModelList).map(ResponseEntity::ok).orElse(notFound().build());
  }

  @Override
  public ResponseEntity<List<User>> getAllCustomers() {
    return ok(uAssembler.toModelList(uService.getAllCustomers()));
  }

  @Override
  public ResponseEntity<User> getCustomerById(String id) {
    return uService.getCustomerById(id).map(uAssembler::toModel).map(ResponseEntity::ok).orElse(notFound().build());
  }

  @Override
  public ResponseEntity<Card> getCardByCustomerId(String id) {
    return uService.getCardByCustomerId(id).map(cAssembler::toModel).map(ResponseEntity::ok).orElse(notFound().build());
  }
}
