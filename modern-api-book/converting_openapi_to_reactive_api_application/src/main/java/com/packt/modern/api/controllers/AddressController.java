package com.packt.modern.api.controllers;

import static org.springframework.http.ResponseEntity.accepted;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.packt.modern.api.AddressApi;
import com.packt.modern.api.hateoas.AddressRepresentationModelAssembler;
import com.packt.modern.api.model.AddAddressReq;
import com.packt.modern.api.model.Address;
import com.packt.modern.api.service.AddressService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
public class AddressController implements AddressApi {

  private final AddressService addressService;
  private final AddressRepresentationModelAssembler assembler;

  public AddressController(AddressService addressService, AddressRepresentationModelAssembler assembler) {
    this.addressService = addressService;
    this.assembler = assembler;
  }

  public Mono<ResponseEntity<Address>> createAddress(@Valid Mono<AddAddressReq> addAddressReq, ServerWebExchange exchange) {
    return addressService.createAddress(addAddressReq.cache())
    .map(add -> status(HttpStatus.CREATED).body(assembler.entityToModel(add, exchange)));
  }

  @Override
  public Mono<ResponseEntity<Address>> getAddressesById(String id, ServerWebExchange exchange) {
    return addressService.getAddressById(id)
    .map(add -> ok(assembler.entityToModel(add, exchange)))
    .defaultIfEmpty(notFound().build());
  }

  @Override
  public Mono<ResponseEntity<Flux<Address>>> getAllAddresses(ServerWebExchange exchange) {
    return Mono.just(ok(assembler.toListModel(addressService.getAllAddresses(), exchange)));
  }

  @Override
  public Mono<ResponseEntity<Void>> deleteAddressesById(String id, ServerWebExchange exchange) {
    addressService.deleteAddressById(id).subscribeOn(Schedulers.boundedElastic());
    return Mono.just(accepted().build());
  }
}
