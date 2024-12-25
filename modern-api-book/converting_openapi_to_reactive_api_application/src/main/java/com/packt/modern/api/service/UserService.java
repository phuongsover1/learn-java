package com.packt.modern.api.service;

import com.packt.modern.api.entity.AddressEntity;
import com.packt.modern.api.entity.CardEntity;
import com.packt.modern.api.entity.UserEntity;
import com.packt.modern.api.model.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
  Mono<Void> deleteCustomerById(String id);

  Flux<AddressEntity> getAddressesByCustomerId(String id);

  Flux<UserEntity> getAllCustomers();

  Mono<CardEntity> getCardByCustomerId(String id);

  Mono<UserEntity> getCustomerById(String id);

  User toModel(UserEntity entity);

  UserEntity toEntity(User model);

  Mono<Void> deleteAddressOfCustomer(String customerId, String addressId);
}
