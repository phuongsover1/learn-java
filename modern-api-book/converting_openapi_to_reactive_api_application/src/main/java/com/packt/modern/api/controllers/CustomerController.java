package com.packt.modern.api.controllers;

import static org.springframework.http.ResponseEntity.*;

import com.packt.modern.api.CustomerApi;
import com.packt.modern.api.exceptions.AddressNotFoundException;
import com.packt.modern.api.exceptions.ResourceNotFoundException;
import com.packt.modern.api.hateoas.AddressRepresentationModelAssembler;
import com.packt.modern.api.hateoas.CardRepresentationModelAssembler;
import com.packt.modern.api.hateoas.UserRepresentationModelAssembler;
import com.packt.modern.api.model.Address;
import com.packt.modern.api.model.Card;
import com.packt.modern.api.model.User;
import com.packt.modern.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CustomerController implements CustomerApi {
  private final UserService uService;
  private final UserRepresentationModelAssembler uAssembler;
  private final AddressRepresentationModelAssembler aAssembler;
  private final CardRepresentationModelAssembler cAssembler;

  public CustomerController(
      UserService uService,
      UserRepresentationModelAssembler assembler,
      AddressRepresentationModelAssembler aAssembler,
      CardRepresentationModelAssembler cAssembler) {
    this.uService = uService;
    this.uAssembler = assembler;
    this.aAssembler = aAssembler;
    this.cAssembler = cAssembler;
  }

  @Override
  public Mono<ResponseEntity<Void>> deleteCustomerById(String id, ServerWebExchange exchange) {
    return uService
        .getCustomerById(id)
        .flatMap(
            u ->
                uService
                    .deleteCustomerById(id)
                    .then(Mono.just(status(HttpStatus.ACCEPTED).<Void>build())))
        .switchIfEmpty(Mono.just(notFound().build()));
  }

  @Override
  public Mono<ResponseEntity<Flux<Address>>> getAddressesByCustomerId(
      String id, ServerWebExchange exchange) {
    return Mono.just(
        ok(
            uService
                .getAddressesByCustomerId(id)
                .map(c -> aAssembler.entityToModel(c, exchange))
                .switchIfEmpty(
                    Mono.error(
                        new ResourceNotFoundException("No address found for given customer")))));
  }

  @Override
  public Mono<ResponseEntity<Flux<User>>> getAllCustomers(ServerWebExchange exchange) {
    return Mono.just(ok(uAssembler.toListModel(uService.getAllCustomers(), exchange)));
  }

  @Override
  public Mono<ResponseEntity<User>> getCustomerById(String id, ServerWebExchange exchange) {
    return uService
        .getCustomerById(id)
        .map(u -> uAssembler.entityToModel(u, exchange))
        .map(ResponseEntity::ok)
        .defaultIfEmpty(notFound().build());
  }

  @Override
  public Mono<ResponseEntity<Card>> getCardByCustomerId(String id, ServerWebExchange exchange) {
    return uService
        .getCardByCustomerId(id)
        .map(card -> cAssembler.entityToModel(card, exchange))
        .map(ResponseEntity::ok)
        .defaultIfEmpty(notFound().build());
  }

  @Override
  public Mono<ResponseEntity<Void>> deleteAddressOfCustomer(
      String customerId, String addressId, ServerWebExchange exchange) throws Exception {
    return uService
        .deleteAddressOfCustomer(customerId, addressId)
        .onErrorStop()
        .then(Mono.just(status(HttpStatus.ACCEPTED).<Void>build()));
  }
}
