package com.packt.modern.api.service;

import com.packt.modern.api.entity.AddressEntity;
import com.packt.modern.api.entity.CardEntity;
import com.packt.modern.api.entity.UserEntity;
import com.packt.modern.api.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void deleteCustomerById(String id) {
    userRepository.deleteById(UUID.fromString(id));
  }

  @Override
  public Optional<Iterable<AddressEntity>> getAddressesByCustomerId(String id) {
    return userRepository.findById(UUID.fromString(id)).map(UserEntity::getAddresses);
  }

  @Override
  public Iterable<UserEntity> getAllCustomers() {
    return userRepository.findAll();
  }

  @Override
  public Optional<CardEntity> getCardByCustomerId(String id) {
    return Optional.of(
        userRepository.findById(UUID.fromString(id)).map(UserEntity::getCard).get().get(0));
  }

  @Override
  public Optional<UserEntity> getCustomerById(String id) {
    return userRepository.findById(UUID.fromString(id));
  }

  @Override
  public UserEntity findUserByUsername(String username) {
    if (Strings.isBlank(username)) {
      throw new UsernameNotFoundException("Invalid user");
    }
    final String uname = username.trim();
    Optional<UserEntity> oUserEntity = userRepository.findByUsername(uname);
    return oUserEntity.orElseThrow(
        () -> new UsernameNotFoundException(String.format("Given user %s not found.", uname)));
  }
}
