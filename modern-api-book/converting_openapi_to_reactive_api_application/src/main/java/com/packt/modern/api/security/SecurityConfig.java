package com.packt.modern.api.security;

import static com.packt.modern.api.security.Constants.*;

import java.util.List;
import java.util.Map;

import com.packt.modern.api.entity.RoleEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

  @Bean
  protected SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, ShallowEtagHeaderFilter shallowEtagHeaderFilter) {
    http.httpBasic()
        .disable()
        .formLogin()
        .disable()
        .csrf()
        .disable()
        .headers(headerSpec -> headerSpec.frameOptions(frameOptions -> frameOptions.disable()))
        .cors()
        .and()
        .authorizeExchange(
            req ->
                req.pathMatchers(HttpMethod.POST, TOKEN_URL)
                    .permitAll()
                    .pathMatchers(HttpMethod.DELETE, TOKEN_URL)
                    .permitAll()
                    .pathMatchers(HttpMethod.POST, SIGNUP_URL)
                    .permitAll()
                    .pathMatchers(HttpMethod.POST, REFRESH_URL)
                    .permitAll()
                    .pathMatchers(HttpMethod.GET, PRODUCTS_URL)
                    .permitAll()
                    .pathMatchers(ACTUATOR_URL_PREFIX)
                    .permitAll()
//                    .pathMatchers("/api/v1/addresses/**")
//                    .hasAuthority(RoleEnum.ADMIN.getAuthority())
                    .anyExchange()
                    .authenticated())
        .oauth2ResourceServer(
            oAuth2ResourceServerSpec ->
                oAuth2ResourceServerSpec.jwt(
                    jwt -> jwt.jwtAuthenticationConverter(getJwtAuthenticationConverter())));
    return http.build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("*"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "HEAD", "PATCH"));
    // For CORS response heasers
    configuration.addAllowedOrigin("*");
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  private Converter<Jwt, Mono<AbstractAuthenticationToken>> getJwtAuthenticationConverter() {
    var authoritiesConverter = new JwtGrantedAuthoritiesConverter();
    authoritiesConverter.setAuthorityPrefix(AUTHORITY_PREFIX);
    authoritiesConverter.setAuthoritiesClaimName(ROLE_CLAIM);
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
    return new ReactiveJwtAuthenticationConverterAdapter(converter);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    Map<String, PasswordEncoder> encoders = Map.of(
            ENCODER_ID, new BCryptPasswordEncoder(),
            "pbkdf2", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8(),
            "scrypt", SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8()
    );
    return new DelegatingPasswordEncoder(ENCODER_ID, encoders);
  }
}
