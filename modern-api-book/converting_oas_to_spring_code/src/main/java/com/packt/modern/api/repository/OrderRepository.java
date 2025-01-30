package com.packt.modern.api.repository;

import com.packt.modern.api.entity.OrderEntity;
import com.packt.modern.api.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.UUID;

public interface OrderRepository extends CrudRepository<OrderEntity, UUID>, OrderRepositoryExt {

  @Query("select o from OrderEntity o join o.userEntity u where u.id = :customerId")
  Iterable<OrderEntity> findByCustomerId(@Param("customerId") UUID customerId);

  //　TODO: Tạo ra trường hợp N+1 để xem thử
  Iterable<OrderEntity> findAllByUserEntityIsIn(Collection<UserEntity> userEntities);
}
