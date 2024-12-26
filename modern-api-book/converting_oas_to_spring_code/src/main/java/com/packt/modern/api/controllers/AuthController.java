package com.packt.modern.api.controllers;

import com.packt.modern.api.CustomerApi;
import com.packt.modern.api.entity.UserEntity;
import com.packt.modern.api.exceptions.InvalidRefreshTokenException;
import com.packt.modern.api.model.RefreshToken;
import com.packt.modern.api.model.SignInReq;
import com.packt.modern.api.model.SignedInUser;
import com.packt.modern.api.model.User;
import com.packt.modern.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.*;

@RestController
public class AuthController implements CustomerApi {
  private final UserService uService;
  private final PasswordEncoder passwordEncoder;

  public AuthController(UserService uService, PasswordEncoder passwordEncoder) {
    this.uService = uService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public ResponseEntity<SignedInUser> signIn(SignInReq signInReq) {
    UserEntity userEntity = uService.findUserByUsername(signInReq.getUsername());
    if (passwordEncoder.matches(signInReq.getPassword(), userEntity.getPassword())) {
      return ok(uService.getSignedInUser(userEntity));
    }
    throw new InsufficientAuthenticationException("Unauthorized");
  }

  @Override
  public ResponseEntity<Void> signOut(RefreshToken refreshToken) {
    uService.removeRefreshToken(refreshToken);
    return accepted().build();
  }

  @Override
  public ResponseEntity<SignedInUser> signUp(User user) {
    return status(HttpStatus.CREATED)
            .body(uService.createUser(user).get());
  }

  @Override
  public ResponseEntity<SignedInUser> getAccessToken(RefreshToken refreshToken) {
    return ok(uService.getAccessToken(refreshToken).orElseThrow(InvalidRefreshTokenException::new));
  }
}
