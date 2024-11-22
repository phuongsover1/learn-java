package com.packt.modern.api.repository;

import com.packt.modern.api.model.NewOrder;
import com.packt.modern.api.entity.OrderEntity;

import java.util.Optional;

public interface OrderRepositoryExt {
  Optional<OrderEntity> insert(NewOrder m);
}
