package com.packt.modern.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.packt.modern.api.config.AppConfig;

import java.time.Instant;
import java.util.Base64;
import java.util.Objects;

public class TestUtils {
  private static ObjectMapper objectMapper;

  public static boolean isTokenExpired(String jwt) throws JsonProcessingException {
    var encodePayload =  jwt.split("\\.")[1];
    var payload = new String(Base64.getDecoder().decode(encodePayload));
    JsonNode parent = new ObjectMapper().readTree(payload);
    String expiration = parent.path("exp").asText();
    Instant exptTime = Instant.ofEpochMilli(Long.valueOf(expiration) * 1000);
    return Instant.now().isAfter(exptTime);
  }

  public static ObjectMapper objectMapper() {
    if (Objects.isNull(objectMapper())) {
      objectMapper = new AppConfig().objectMapper();
      objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }
    return objectMapper;
  }
}
