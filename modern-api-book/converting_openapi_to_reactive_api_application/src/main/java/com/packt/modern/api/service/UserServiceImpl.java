package com.packt.modern.api.service;

import com.packt.modern.api.entity.AddressEntity;
import com.packt.modern.api.entity.CardEntity;
import com.packt.modern.api.entity.UserEntity;
import com.packt.modern.api.exceptions.CustomerNotFoundException;
import com.packt.modern.api.exceptions.ErrorCode;
import com.packt.modern.api.repository.CardRepository;
import com.packt.modern.api.repository.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final CardRepository cRepo;

  public UserServiceImpl(UserRepository userRepository, CardRepository cRepo) {
    this.userRepository = userRepository;
    this.cRepo = cRepo;
  }

  @Override
  public Mono<Void> deleteCustomerById(String id) {
    return userRepository.deleteById(UUID.fromString(id));
  }

  @Override
  public Flux<AddressEntity> getAddressesByCustomerId(String id) {
    return userRepository.getAddressesByCustomerId(UUID.fromString(id));
  }

  @Override
  public Flux<UserEntity> getAllCustomers() {
    return userRepository.findAll();
  }

  @Override
  public Mono<CardEntity> getCardByCustomerId(String id) {
    return userRepository.findById(UUID.fromString(id))
    .switchIfEmpty(Mono.error(new CustomerNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND)))
    .flatMap(u -> cRepo.findByUserId(u.getId()));
  }

  @Override
  public Mono<UserEntity> getCustomerById(String id) {
    return userRepository.findById(UUID.fromString(id));
  }
}
