package com.packt.modern.api.service;

import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.packt.modern.api.entity.CardEntity;
import com.packt.modern.api.exceptions.CustomerNotFoundException;
import com.packt.modern.api.model.AddCardReq;
import com.packt.modern.api.repository.CardRepository;
import com.packt.modern.api.repository.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CardServiceImpl implements CardService {
  private final CardRepository repo;
  private final UserRepository userRepo;

  public CardServiceImpl(CardRepository repo, UserRepository userRepo) {
    this.repo = repo;
    this.userRepo = userRepo;
  }

  @Override
  public Mono<Void> deleteCardById(String id) {
    return repo.deleteById(UUID.fromString(id));
  }

  @Override
  public Flux<CardEntity> getAllCards() {
    return repo.findAll();
  }

  @Override
  public Mono<CardEntity> getCardById(String id) {
    return repo.findById(UUID.fromString(id));
  }

  @Override
  public Mono<CardEntity> registerCard(Mono<AddCardReq> addCardReq) {
    return Mono.empty();
  }

  private CardEntity toEntity(AddCardReq addCardReq) {
    CardEntity card = new CardEntity();
    BeanUtils.copyProperties(addCardReq, card);
    userRepo.findById(UUID.fromString(addCardReq.getUserId()))
    .switchIfEmpty(Mono.error(new CustomerNotFoundException(String.format(
      "User with id %s not founded.", addCardReq.getUserId()))))
    .map(u -> card.setUserId(u.getId()))
    .then().block();
    return card;
  }

}
