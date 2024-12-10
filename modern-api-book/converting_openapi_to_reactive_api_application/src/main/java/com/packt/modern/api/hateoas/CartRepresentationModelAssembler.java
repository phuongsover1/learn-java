package com.packt.modern.api.hateoas;

import com.packt.modern.api.entity.CartEntity;
import com.packt.modern.api.entity.ItemEntity;
import com.packt.modern.api.model.Cart;
import com.packt.modern.api.model.Item;
import jakarta.annotation.Nullable;
import org.apache.logging.log4j.util.Strings;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.reactive.ReactiveRepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Component
public class CartRepresentationModelAssembler implements
    ReactiveRepresentationModelAssembler<CartEntity, Cart>, HateoasSupport {

  private static String serverUri = null;

  private String getServerUri(@Nullable ServerWebExchange exchange) {
    if (Strings.isBlank(serverUri)) {
      serverUri = getUriComponentsBuilder(exchange).toUriString();
    }
    return serverUri;
  }

  @Override
  public Mono<Cart> toModel(CartEntity entity, ServerWebExchange exchange) {
    return Mono.just(entityToModel(entity, exchange));
  }

  private Cart entityToModel(CartEntity entity, ServerWebExchange exchange) {
    Cart resource = new Cart();
    if (Objects.isNull(entity)) {
      return resource;
    }
    resource.id(entity.getId().toString())
        .customerId(entity.getUser().getId().toString())
        .items(itemFromEntitities(entity.getItems()));
    String serverUri = getServerUri(exchange);
    resource
        .add(Link.of(String.format("%s/api/v1/carts/%s", serverUri, entity.getId().toString())).withSelfRel());
    return resource;
  }

  private List<Item> itemFromEntitities(List<ItemEntity> items) {
    return items.stream().map(
            i -> new Item().id(i.getProduct().getId().toString())
                .unitPrice(i.getPrice())
                .quantity(i.getQuantity())
        )
        .toList();
  }

  public Flux<Cart> toListModel(Flux<CartEntity> entities, ServerWebExchange exchange) {
    if (Objects.isNull(entities)) {
      return Flux.empty();
    }
    return Flux.from(entities.map(e -> entityToModel(e, exchange)));
  }

}
