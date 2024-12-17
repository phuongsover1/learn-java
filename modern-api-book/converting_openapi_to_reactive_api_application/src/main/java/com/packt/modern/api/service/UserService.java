package com.packt.modern.api.service;

import com.packt.modern.api.entity.AddressEntity;
import com.packt.modern.api.entity.CardEntity;
import com.packt.modern.api.entity.UserEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface UserService {
  Mono<Void> deleteCustomerById(String id);
  Flux<AddressEntity> getAddressesByCustomerId(String id);
  Flux<UserEntity> getAllCustomers();
  Mono<CardEntity> getCardByCustomerId(String id);
  Mono<UserEntity> getCustomerById(String id);

}
