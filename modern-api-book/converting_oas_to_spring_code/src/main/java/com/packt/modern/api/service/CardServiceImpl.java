package com.packt.modern.api.service;

import com.packt.modern.api.entity.CardEntity;
import com.packt.modern.api.entity.UserEntity;
import com.packt.modern.api.model.AddCardReq;
import com.packt.modern.api.repository.CardRepository;
import com.packt.modern.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CardServiceImpl implements CardService {
  private final CardRepository repo;
  private final UserRepository userRepo;

  public CardServiceImpl(CardRepository repo, UserRepository userRepo) {
    this.repo = repo;
    this.userRepo = userRepo;
  }

  @Override
  public void deleteCardById(String id) {
    repo.deleteById(UUID.fromString(id));
  }

  @Override
  public Iterable<CardEntity> getAllCards() {
    return repo.findAll();
  }

  @Override
  public Optional<CardEntity> getCardById(String id) {
    return repo.findById(UUID.fromString(id));
  }

  @Override
  public Optional<CardEntity> registerCard(AddCardReq addCardReq) {
    return Optional.empty();
  }

  private CardEntity toEntity(AddCardReq addCardReq) {
    CardEntity card = new CardEntity();
    Optional<UserEntity> user = userRepo.findById(UUID.fromString(addCardReq.getUserId()));
    user.ifPresent(card::setUser);
    return card.setNumber(addCardReq.getCardNumber()).setCvv(addCardReq.getCvv()).setExpires(addCardReq.getExpires());
  }
}
