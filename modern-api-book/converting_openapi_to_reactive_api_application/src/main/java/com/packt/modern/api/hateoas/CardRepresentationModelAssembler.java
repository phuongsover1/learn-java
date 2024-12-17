package com.packt.modern.api.hateoas;

import com.packt.modern.api.entity.CardEntity;
import com.packt.modern.api.model.Card;
import jakarta.annotation.Nullable;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.reactive.ReactiveRepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class CardRepresentationModelAssembler implements
    ReactiveRepresentationModelAssembler<CardEntity, Card>, HateoasSupport {

  private static String serverUri = null;

  private String getServerUri(@Nullable ServerWebExchange exchange) {
    if (Strings.isBlank(serverUri)) {
      serverUri = getUriComponentsBuilder(exchange).toUriString();
    }
    return serverUri;
  }

  /**
   * Converts card entity to resource
   * 
   * @param entity
   * @param exchange
   * @return
   */
  @Override
  public Mono<Card> toModel(CardEntity entity, ServerWebExchange exchange) {
    return Mono.just(entityToModel(entity, exchange));
  }

  public Card entityToModel(CardEntity entity, ServerWebExchange exchange) {
    Card resource = new Card();
    if (Objects.isNull(entity)) {
      return resource;
    }

    BeanUtils.copyProperties(entity, resource);
    resource.setId(Objects.nonNull(entity.getId()) ? entity.getId().toString() : "");
    resource.setCardNumber(entity.getNumber());
    resource.setUserId(Objects.nonNull(entity.getUserId()) ? entity.getUserId().toString() : "");
    String serverUri = getServerUri(exchange);

    resource.add(Link.of(String.format("%s/api/v1/cards", serverUri)).withRel("cards"));
    resource.add(
        Link.of(String.format("%s/api/v1/cards/%s", serverUri, entity.getId())).withRel("self"));

    return resource;
  }

  /**
   * Converts List of card entity to list of resource
   * 
   * @param entities
   * @param exchange
   * @return list of card resource
   */
  public Flux<Card> tolistModel(Flux<CardEntity> entities, ServerWebExchange exchange) {
    if (Objects.isNull(entities)) {
      return Flux.empty();
    }
    return Flux.from(entities.map(e -> entityToModel(e, exchange)));
  }
}
