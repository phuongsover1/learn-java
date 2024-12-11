package com.packt.modern.api.repository;

import com.packt.modern.api.model.NewOrder;
import com.packt.modern.api.entity.OrderEntity;
import reactor.core.publisher.Mono;


public interface OrderRepositoryExt {
  Mono<OrderEntity> insert(Mono<NewOrder> m);

  Mono<OrderEntity> updateMapping(OrderEntity orderEntity);
}
