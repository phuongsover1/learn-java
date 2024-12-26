package com.packt.modern.api.service;

import com.packt.modern.api.entity.AddressEntity;
import com.packt.modern.api.entity.CardEntity;
import com.packt.modern.api.entity.UserEntity;
import com.packt.modern.api.model.RefreshToken;
import com.packt.modern.api.model.SignedInUser;
import com.packt.modern.api.model.User;

import java.util.Optional;

public interface UserService {
  void deleteCustomerById(String id);
  Optional<Iterable<AddressEntity>> getAddressesByCustomerId(String id);
  Iterable<UserEntity> getAllCustomers();
  Optional<CardEntity> getCardByCustomerId(String id);
  Optional<UserEntity> getCustomerById(String id);
  UserEntity findUserByUsername(String username);
  Optional<SignedInUser> createUser(User user);
  SignedInUser getSignedInUser(UserEntity userEntity);
  Optional<SignedInUser> getAccessToken(RefreshToken refToken);
  void removeRefreshToken(RefreshToken refToken);
}
