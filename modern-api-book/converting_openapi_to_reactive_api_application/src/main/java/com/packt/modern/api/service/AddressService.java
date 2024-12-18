package com.packt.modern.api.service;

import com.packt.modern.api.model.AddAddressReq;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.packt.modern.api.entity.AddressEntity;


public interface AddressService {
  Mono<AddressEntity> createAddress(Mono<AddAddressReq> addAddressReq);

  Mono<Void> deleteAddressById(String id);

  Mono<AddressEntity> getAddressById(String id);

  Flux<AddressEntity> getAllAddresses();
}
