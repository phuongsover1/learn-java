package com.packt.modern.api.hateoas;

import com.packt.modern.api.entity.AddressEntity;
import com.packt.modern.api.model.Address;
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
public class AddressRepresentationModelAssembler implements
    ReactiveRepresentationModelAssembler<AddressEntity, Address>, HateoasSupport {

  private static String serverUri = null;

  /**
   * Converts the Address entity to resource
   *
   * @param entity
   * @param exchange
   * @return address resource
   */
  @Override
  public Mono<Address> toModel(AddressEntity entity, ServerWebExchange exchange) {
    return Mono.just(entityToModel(entity, exchange));
  }

  private String getServerUri(@Nullable ServerWebExchange exchange) {
    if (Strings.isBlank(serverUri)) {
      serverUri = getUriComponentsBuilder(exchange).toUriString();
    }
    return serverUri;
  }

  public Address entityToModel(AddressEntity entity, ServerWebExchange exchange) {
    Address resource = new Address();
    if (Objects.isNull(entity)) {
      return resource;
    }
    BeanUtils.copyProperties(entity, resource);
    resource.setId(entity.getId().toString());
    String serverUri = getServerUri(exchange);

    resource.add(Link.of(String.format("%s/api/v1/addresses", serverUri)).withRel("addresses"));
    resource.add(Link.of(String.format("%s/api/v1/addresses/%s", serverUri, resource.getId())).withSelfRel());
    return resource;
  }

  /**
   * Converts the collection of Address entities to list of resource
   * @param entities
   * @param exchange
   * @return list of address resource
   */
  public Flux<Address> toListModel(Flux<AddressEntity> entities, ServerWebExchange exchange) {
    if (Objects.isNull(entities)) {
      return Flux.empty();
    }

    return Flux.from(entities.map(e -> entityToModel(e, exchange)));
  }
}
