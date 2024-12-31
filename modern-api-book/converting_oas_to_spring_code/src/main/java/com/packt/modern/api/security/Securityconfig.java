package com.packt.modern.api.security;

import static com.packt.modern.api.security.Constants.*;
import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import com.packt.modern.api.entity.RoleEnum;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@SuppressWarnings("deprecation")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Securityconfig {

  private final Logger LOG = LoggerFactory.getLogger(getClass());

  @Value("${app.security.jwt.keystore-location}")
  private String keyStorePath;

  @Value("${app.security.jwt.keystore-password}")
  private String keyStorePassword;

  @Value("${app.security.jwt.key-alias}")
  private String keyAlias;

  @Value("${app.security.jwt.private-key-passphrase}")
  private String privateKeyPassphrase;


  @Bean
  public KeyStore keyStore() {
    try {
      KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      InputStream resStream =
          Thread.currentThread().getContextClassLoader().getResourceAsStream(keyStorePath);
      keyStore.load(resStream, keyStorePassword.toCharArray());
      return keyStore;
    } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
      LOG.error("Unable to load keystore: {}", keyStorePath, e);
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
      LOG.error("key from keystore: {}", keyStorePath, e);
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
      LOG.error("key from keystore: {}", keyStorePath, e);
    }
    throw new IllegalArgumentException("Can't load public key");
  }

  @Bean
  public JwtDecoder jwtDecoder(RSAPublicKey publicKey) {
    return NimbusJwtDecoder.withPublicKey(publicKey).build();
  }

  @Bean
  protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.httpBasic()
        .disable()
        .formLogin()
        .disable()
        .csrf()
        .ignoringRequestMatchers(API_URL_PREFIX)
        .ignoringRequestMatchers(toH2Console())
        .and()
        .headers()
        .frameOptions()
        .sameOrigin()
        .and()
        .cors()
        .and()
        .authorizeHttpRequests(
            req ->
                req.requestMatchers(toH2Console())
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher(TOKEN_URL, HttpMethod.POST.name()))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher(TOKEN_URL, HttpMethod.DELETE.name()))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher(SIGNUP_URL, HttpMethod.POST.name()))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher(REFRESH_URL, HttpMethod.POST.name()))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher(PRODUCTS_URL, HttpMethod.GET.name()))
                    .permitAll()
                    .requestMatchers("/api/v1/addresses/**")
                    .hasAuthority(RoleEnum.ADMIN.getAuthority())
                    .anyRequest()
                    .authenticated())
        .oauth2ResourceServer(
            oauth2ResourceServer ->
                oauth2ResourceServer.jwt(
                    jwt -> jwt.jwtAuthenticationConverter(getJwtAuthenticationConverter())))
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    return http.build();
  }
//  @Bean
//  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//   return authenticationConfiguration.getAuthenticationManager();
//  }
//
//  @Bean
//  public UserDetailsService userDetailsService() {
//    return userService;
//  }
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("*"));
    configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH"));
    // configuration.setAllowCredentials(true);
    // For CORS response headers
    configuration.addAllowedOrigin("*");
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }


   private Converter<Jwt, AbstractAuthenticationToken> getJwtAuthenticationConverter() {
     JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
     authoritiesConverter.setAuthorityPrefix(AUTHORITY_PREFIX);
     authoritiesConverter.setAuthoritiesClaimName(ROLE_CLAIM);
     JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
     converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
     return converter;
   }
}
