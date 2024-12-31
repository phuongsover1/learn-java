package com.packt.modern.api.security;

import static com.packt.modern.api.security.Constants.EXPIRATION_TIME;
import static com.packt.modern.api.security.Constants.ROLE_CLAIM;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;

@Component
public class JwtManager {

  private final Logger LOG  = LoggerFactory.getLogger(this.getClass());

  private final RSAPublicKey publicKey;
  private final RSAPrivateKey privateKey;

  @Value("${app.security.jwt.keystore-location}")
  private String keyStorePath;

  @Value("${app.security.jwt.keystore-password}")
  private String keyStorePassword;

  @Value("${app.security.jwt.key-alias}")
  private String keyAlias;

  @Value("${app.security.jwt.private-key-passphrase}")
  private String privateKeyPassphrase;

  public JwtManager(@Lazy RSAPublicKey publicKey,@Lazy RSAPrivateKey privateKey) {
    this.publicKey = publicKey;
    this.privateKey = privateKey;
  }

  @Bean
  public KeyStore keyStore(){
    try {
      KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      InputStream resStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(keyStorePath);
      keyStore.load(resStream, keyStorePassword.toCharArray());
      return keyStore;
    } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
      LOG.error("Unable to load keystore {}", keyStorePath, e);
    }
    throw new IllegalArgumentException("Can't load keystore");
  }

  @Bean
  public RSAPrivateKey jwtSigningKey(KeyStore keyStore) {
    try {
      Key key = keyStore.getKey(keyAlias, privateKeyPassphrase.toCharArray());
      if (key instanceof RSAPrivateKey) {
        return (RSAPrivateKey) key;
      }
    } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
      LOG.error("key from keystore {}", keyStorePath, e);
    }

    throw new IllegalArgumentException("Can't load private key");
  }

  @Bean
  public RSAPublicKey jwtValidationKey(KeyStore keyStore) {
    try {
      Certificate certificate = keyStore.getCertificate(keyAlias);
      PublicKey publicKey = certificate.getPublicKey();
      if (publicKey instanceof RSAPublicKey) {
        return (RSAPublicKey) publicKey;
      }
    } catch (KeyStoreException e) {
      LOG.error("key from keystore {}", keyStorePath, e);
    }
    throw new IllegalArgumentException("Can't load public key");
  }

  @Bean
  public ReactiveJwtDecoder jwtDecoder(RSAPublicKey publicKey) {
    return NimbusReactiveJwtDecoder.withPublicKey(publicKey).build();
  }

  public String create(UserDetails principal) {
    final long now = System.currentTimeMillis();
    return JWT.create()
            .withIssuer("Modern API Development with Spring...")
            .withSubject(principal.getUsername())
            .withClaim(ROLE_CLAIM, principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
            .withIssuedAt(new Date(now))
            .withExpiresAt(new Date(now + EXPIRATION_TIME))
            .sign(Algorithm.RSA256(publicKey, privateKey));
  }
}
