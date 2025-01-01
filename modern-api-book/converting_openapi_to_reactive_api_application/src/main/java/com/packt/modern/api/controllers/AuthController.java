package com.packt.modern.api.controllers;

import static org.springframework.http.ResponseEntity.status;

import com.packt.modern.api.UserApi;
import com.packt.modern.api.model.RefreshToken;
import com.packt.modern.api.model.SignInReq;
import com.packt.modern.api.model.SignedInUser;
import com.packt.modern.api.model.User;
import com.packt.modern.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
public class AuthController implements UserApi {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Mono<ResponseEntity<SignedInUser>> signIn(
      Mono<SignInReq> signInReq, ServerWebExchange exchange) throws Exception {
    return userService.signIn(signInReq.cache()).map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<SignedInUser>> signUp(Mono<User> user, ServerWebExchange exchange)
      throws Exception {
    return userService.createUser(user).map(u -> status(HttpStatus.CREATED).body(u));
  }

  @Override
  public Mono<ResponseEntity<SignedInUser>> getAccessToken(
      Mono<RefreshToken> refreshToken, ServerWebExchange exchange) throws Exception {
    return userService.getAccessToken(refreshToken.cache()).map(u -> status(HttpStatus.OK).body(u));
  }

  @Override
  public Mono<ResponseEntity<Void>> signOut(
      Mono<RefreshToken> refreshToken, ServerWebExchange exchange) throws Exception {
    return userService
        .removeRefreshToken(refreshToken.cache())
        .then(Mono.just(status(HttpStatus.OK).build()));
  }
}
