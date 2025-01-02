package com.packt.modern.api.service;

import com.packt.modern.api.entity.AddressEntity;
import com.packt.modern.api.entity.CardEntity;
import com.packt.modern.api.entity.UserEntity;
import com.packt.modern.api.entity.UserTokenEntity;
import com.packt.modern.api.exceptions.*;
import com.packt.modern.api.model.RefreshToken;
import com.packt.modern.api.model.SignInReq;
import com.packt.modern.api.model.SignedInUser;
import com.packt.modern.api.model.User;
import com.packt.modern.api.repository.*;
import com.packt.modern.api.security.JwtManager;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final CardRepository cRepo;
  private final UserAddressRepository UARepo;
  private final AddressRepository ARepo;
  private final JwtManager jwtManager;
  private final PasswordEncoder passwordEncoder;
  private final UserTokenRepository userTokenRepository;

  public UserServiceImpl(
      UserRepository userRepository,
      CardRepository cRepo,
      UserAddressRepository uaRepo,
      AddressRepository aRepo,
      JwtManager jwtManager,
      PasswordEncoder passwordEncoder,
      UserTokenRepository userTokenRepository) {
    this.userRepository = userRepository;
    this.cRepo = cRepo;
    UARepo = uaRepo;
    ARepo = aRepo;
    this.jwtManager = jwtManager;
    this.passwordEncoder = passwordEncoder;
    this.userTokenRepository = userTokenRepository;
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
    return userRepository
        .findById(UUID.fromString(id))
        .switchIfEmpty(Mono.error(new CustomerNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND)))
        .flatMap(u -> cRepo.findByUserId(u.getId()));
  }

  @Override
  public Mono<UserEntity> getCustomerById(String id) {
    return userRepository.findById(UUID.fromString(id));
  }

  public User toModel(UserEntity entity) {
    User t = new User();
    BeanUtils.copyProperties(entity, t);
    t.setFirstName(entity.getFirstName());
    t.setLastName(entity.getLastName());
    t.setUserStatus(entity.getUserStatus());
    return t;
  }

  // Đang làm chức năng lưu địa chỉ có hoặc không kèm theo user  -> DONE
  // TODO: xoá địa chỉ thì xoá trong bảng user_adress trước -> cần id địa chỉ + id user
  // FIXME: Nếu address cần xoá mà không có add User nào thì xoá bình thường

  public UserEntity toEntity(User model) {
    UserEntity t = new UserEntity();
    BeanUtils.copyProperties(model, t);
    t.setFirstName(model.getFirstName());
    t.setLastName(model.getLastName());
    t.setUserStatus(model.getUserStatus());
    return t;
  }

  @Override
  public Mono<Void> deleteAddressOfCustomer(String customerId, String addressId) {
    Mono<UserEntity> uMono = userRepository.findById(UUID.fromString(customerId));
    Mono<AddressEntity> aMono = ARepo.findById(UUID.fromString(addressId));
    return uMono
        .switchIfEmpty(Mono.error(new CustomerNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND)))
        .flatMap(
            u -> {
              return aMono
                  .switchIfEmpty(
                      Mono.error(new AddressNotFoundException(ErrorCode.ADDRESS_NOT_FOUND)))
                  .flatMap(
                      a -> {
                        return UARepo.findByUserIdAndAddressId(u.getId(), a.getId())
                            .switchIfEmpty(
                                Mono.error(new UserNotInAdress(ErrorCode.USER_NOT_IN_ADDRESS)))
                            .flatMap(
                                UA -> {
                                  System.out.println(UA.getUserId());
                                  return UARepo.deleteAddressOfUser(
                                      UA.getUserId(), UA.getAddressId());
                                });
                      });
            });
  }

  @Override
  public Mono<UserEntity> findUserByUsername(String username) {
    if (Strings.isBlank(username)) {
      return Mono.error(
          new CustomerNotFoundException(String.format("Username %s not found", username)));
    }
    final String uname = username.trim();
    return userRepository
        .findByUsername(uname)
        .switchIfEmpty(
            Mono.error(
                new CustomerNotFoundException(String.format("Username %s not found", uname))));
  }

  @Override
  public Mono<SignedInUser> createUser(Mono<User> userMono) {
    return userMono
        .flatMap(this::checkUsernameAndPassword)
        .flatMap(
            user ->
                userRepository
                    .findByUsernameOrEmail(user.getUsername(), user.getEmail())
                    .flatMap(count -> checkAndCreateUserIfNotExists(count, user))
                    .onErrorStop()
                    .switchIfEmpty(Mono.error(new GenericStatusError("Cannot create user")))
                    .flatMap(this::createSignedInUserWithRefreshToken));
  }

  @Override
  public Mono<SignedInUser> signIn(Mono<SignInReq> signInReqMono) {
    return signInReqMono
        .flatMap(
            signInReq -> {
              if (Strings.isBlank(signInReq.getUsername())) {
                return Mono.error(new GenericStatusError("Username is empty"));
              }
              if (Strings.isBlank(signInReq.getPassword())) {
                return Mono.error(new GenericStatusError("Password is empty"));
              }
              return userRepository
                  .findByUsername(signInReq.getUsername())
                  .switchIfEmpty(
                      Mono.error(
                          new UsernameNotFoundException(
                              "Username " + signInReq.getUsername() + " not found")))
                  .onErrorStop()
                  .zipWith(Mono.just(signInReq.getPassword()));
            })
        .flatMap(
            tuple -> {
              String rawPassword = tuple.getT2();
              UserEntity user = tuple.getT1();
              if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
                return Mono.error(new GenericStatusError("Wrong password"));
              }
              return Mono.just(user);
            })
        .flatMap(this::getSignedInUser);
  }

  @Override
  public Mono<SignedInUser> getSignedInUser(UserEntity userEntity) {
    return userTokenRepository
        .deleteByUserId(userEntity.getId())
        .then(createSignedInUserWithRefreshToken(userEntity));
  }

  @Override
  public Mono<SignedInUser> getAccessToken(Mono<RefreshToken> refreshTokenMono) {
    return refreshTokenMono
        .flatMap(this::validateRefreshToken)
        .flatMap(
            refToken -> userTokenRepository
                .findByRefreshToken(refToken.getRefreshToken())
                .switchIfEmpty(Mono.error(new GenericStatusError("Refresh Token is not exists")))
                .flatMap(userToken -> userRepository.findById(userToken.getUserId()))
                .flatMap(this::createSignedInUser)
                .map(
                    signedInUser -> {
                      signedInUser.setRefreshToken(refToken.getRefreshToken());
                      return signedInUser;
                    }));
  }

  private Mono<RefreshToken> validateRefreshToken(RefreshToken refreshToken) {
    if (Strings.isBlank(refreshToken.getRefreshToken()))
      return Mono.error(new GenericStatusError("Refresh token is empty"));
    return Mono.just(refreshToken);
  }

  @Override
  public Mono<Void> removeRefreshToken(Mono<RefreshToken> refreshTokenMono) {
    return null;
  }

  private Mono<UserEntity> checkAndCreateUserIfNotExists(int count, User user) {
    if (count > 0)
      return Mono.error(new GenericAlreadyExistsException("Use different username or email"));
    return userRepository.save(toEntity(user));
  }

  private Mono<User> checkUsernameAndPassword(User user) {
    if (Strings.isBlank(user.getUsername())) {
      return Mono.error(new GenericStatusError("Username is required"));
    }
    if (Strings.isBlank(user.getPassword())) {
      return Mono.error(new GenericStatusError("Password is required"));
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return Mono.just(user);
  }

  private Mono<SignedInUser> createSignedInUserWithRefreshToken(UserEntity user) {
    return createSignedInUser(user).flatMap(this::createRefreshToken);
  }

  private Mono<SignedInUser> createSignedInUser(UserEntity user) {
    String token =
        jwtManager.create(
            org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(Objects.nonNull(user.getRole()) ? user.getRole() : "")
                .build());
    return Mono.just(
        new SignedInUser()
            .username(user.getUsername())
            .accessToken(token)
            .userId(user.getId().toString()));
  }

  private Mono<SignedInUser> createRefreshToken(SignedInUser signedInUser) {
    String token = RandomHolder.randomKey(128);
    signedInUser.setRefreshToken(token);
    return userTokenRepository
        .save(
            new UserTokenEntity()
                .setRefreshToken(token)
                .setUserId(UUID.fromString(signedInUser.getUserId())))
        .thenReturn(signedInUser);
  }

  private static class RandomHolder {
    static final Random random = new Random();

    public static String randomKey(int length) {
      return String.format(
          "%" + length + "s", new BigInteger(length * 5 /* base32, 2^5 */, random).toString(32));
    }
  }
}
