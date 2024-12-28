package com.packt.modern.api.service;

import com.packt.modern.api.entity.AddressEntity;
import com.packt.modern.api.entity.CardEntity;
import com.packt.modern.api.entity.UserEntity;
import com.packt.modern.api.entity.UserTokenEntity;
import com.packt.modern.api.exceptions.GenericAlreadyExistsException;
import com.packt.modern.api.exceptions.InvalidRefreshTokenException;
import com.packt.modern.api.model.RefreshToken;
import com.packt.modern.api.model.SignedInUser;
import com.packt.modern.api.model.User;
import com.packt.modern.api.repository.UserRepository;
import com.packt.modern.api.repository.UserTokenRepository;
import com.packt.modern.api.security.JwtManager;
import jakarta.transaction.Transactional;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final UserTokenRepository userTokenRepository;
  private final JwtManager tokenManager;

  public UserServiceImpl(
      UserRepository userRepository,
      UserTokenRepository userTokenRepository,
      JwtManager tokenManager) {
    this.userRepository = userRepository;
    this.userTokenRepository = userTokenRepository;
    this.tokenManager = tokenManager;
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

  @Override
  @Transactional
  public Optional<SignedInUser> createUser(User user) {
    checkUsernameAndPassword(user);
    Integer count = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());
    if (count > 0) {
      throw new GenericAlreadyExistsException("Use different username and email.");
    }
    UserEntity userEntity = userRepository.save(toEntity(user));
    return Optional.of(createSignedUserWithRefreshToken(userEntity));
  }

  private void checkUsernameAndPassword(User user) {
    if (Strings.isBlank(user.getUsername())) {
      throw new GenericAlreadyExistsException("Username is empty.");
    }
    if (Strings.isBlank(user.getPassword())) {
      throw new GenericAlreadyExistsException("Password is empty.");
    }
  }

  private SignedInUser createSignedUserWithRefreshToken(UserEntity userEntity) {
    return createSignedInUser(userEntity).refreshToken(createRefreshToken(userEntity));
  }

  private SignedInUser createSignedInUser(UserEntity userEntity) {
    String token =
        tokenManager.create(
            org.springframework.security.core.userdetails.User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(
                    Objects.nonNull(userEntity.getRole()) ? userEntity.getRole().name() : "")
                .build());
    return new SignedInUser()
        .username(userEntity.getUsername())
        .accessToken(token)
        .userId(userEntity.getId().toString());
  }

  private String createRefreshToken(UserEntity userEntity) {
    String token = RandomHolder.randomKey(128);
    userTokenRepository.save(new UserTokenEntity().setRefreshToken(token).setUser(userEntity));
    return token;
  }

  private UserEntity toEntity(User user) {
    UserEntity userEntity = new UserEntity();
    BeanUtils.copyProperties(user, userEntity);
    return userEntity;
  }

  @Override
  public SignedInUser getSignedInUser(UserEntity userEntity) {
    userTokenRepository.deleteByUserId(userEntity.getId());
    return createSignedUserWithRefreshToken(userEntity);
  }

  // Cứ mỗi lần yêu cầu token bằng request token thì trả về access token mới với refresh token cũ
  @Override
  public Optional<SignedInUser> getAccessToken(RefreshToken refToken) {
    return userTokenRepository
        .findByRefreshToken(refToken.getRefreshToken())
        .map(
            ut ->
                Optional.of(
                    createSignedInUser(ut.getUser()).refreshToken(refToken.getRefreshToken())))
        .orElseThrow(() -> new InvalidRefreshTokenException("Invalid token"));
  }

  @Override
  public void removeRefreshToken(RefreshToken refToken) {
    userTokenRepository
        .findByRefreshToken(refToken.getRefreshToken())
        .ifPresentOrElse(
            userTokenRepository::delete,
            () -> {
              throw new InvalidRefreshTokenException("Invalid token");
            });
  }

  private static class RandomHolder {
    static final Random random = new SecureRandom();

    public static String randomKey(int length) {
      return String.format(
          "%" + length + "s", new BigInteger(length * 5 /*base32, 2^5*/, random).toString(32));
    }
  }
}
