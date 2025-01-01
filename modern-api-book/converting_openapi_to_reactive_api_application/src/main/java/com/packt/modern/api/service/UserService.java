package com.packt.modern.api.service;

import com.packt.modern.api.entity.AddressEntity;
import com.packt.modern.api.entity.CardEntity;
import com.packt.modern.api.entity.UserEntity;
import com.packt.modern.api.model.RefreshToken;
import com.packt.modern.api.model.SignInReq;
import com.packt.modern.api.model.SignedInUser;
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

  Mono<UserEntity> findUserByUsername(String username);

  Mono<SignedInUser> createUser(Mono<User> userMono);

  Mono<SignedInUser> getSignedInUser(UserEntity userEntity);

  Mono<SignedInUser> signIn(Mono<SignInReq> signInReqMono);

  Mono<SignedInUser> getAccessToken(Mono<RefreshToken> refreshTokenMono);

  Mono<Void> removeRefreshToken(Mono<RefreshToken> refreshTokenMono);
}
