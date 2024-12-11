package com.packt.modern.api.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.packt.modern.api.entity.CartEntity;
import com.packt.modern.api.entity.ItemEntity;
import com.packt.modern.api.entity.OrderEntity;
import com.packt.modern.api.entity.OrderItemEntity;
import com.packt.modern.api.exceptions.ResourceNotFoundException;
import com.packt.modern.api.model.NewOrder;
import com.packt.modern.api.model.Order.StatusEnum;

import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Row;

import org.springframework.beans.BeanUtils;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.Parameter;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

@Repository
public class OrderRepositoryExtImpl implements OrderRepositoryExt {

  private final ConnectionFactory connectionFactory;
  private final DatabaseClient dbClient;
  private final ItemRepository iRepo;
  private final CartRepository cRepo;
  private final OrderItemRepository oiRepo;
  private final ItemRepository itemRepository;

  public OrderRepositoryExtImpl(ConnectionFactory connectionFactory, DatabaseClient dbClient, ItemRepository iRepo,
      CartRepository cRepo, OrderItemRepository oiRepo, ItemRepository itemRepository) {
    this.connectionFactory = connectionFactory;
    this.dbClient = dbClient;
    this.iRepo = iRepo;
    this.cRepo = cRepo;
    this.oiRepo = oiRepo;
    this.itemRepository = itemRepository;
  }

  private OrderEntity toEntity(NewOrder order, CartEntity c) {
    OrderEntity orderEntity = new OrderEntity();
    BeanUtils.copyProperties(order, orderEntity);
    orderEntity.setUserEntity(c.getUser());
    orderEntity.setCardId(c.getId());
    orderEntity.setItems(c.getItems())
        .setCustomerId(UUID.fromString(order.getCustomerId()))
        .setAddressId(UUID.fromString(order.getAddress().getId()))
        .setOrderDate(Timestamp.from(Instant.now()))
        .setTotal(c.getItems().stream()
            .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
            .reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
    return orderEntity;
  }

  @Override
  public Mono<OrderEntity> insert(Mono<NewOrder> mdl) {
    // Items are already in cart and saved in db when user places the order
    // Here, you can also populate the other Order details like address, card etc.
    AtomicReference<UUID> orderId = new AtomicReference<>();
    Mono<List<ItemEntity>> itemEntities = mdl
        .flatMap(m -> itemRepository.findByCustomerId(UUID.fromString(m.getCustomerId())).collectList().cache());
    Mono<CartEntity> cartEntity = mdl
        .flatMap(m -> cRepo.findByCustomerId(UUID.fromString(m.getCustomerId())).cache());
    cartEntity = Mono.zip(cartEntity, itemEntities,
        (c, i) -> {
          if (i.size() < 1) {
            throw new ResourceNotFoundException(String.format("""
                  There is no item found in customer's (ID: %s) cart.
                """, c.getUser().getId()));
          }
          return c.setItems(i);
        }).cache();

    R2dbcEntityTemplate template = new R2dbcEntityTemplate(connectionFactory);
    Mono<OrderEntity> orderEntity = Mono.zip(mdl,
        cartEntity, (m, c) -> toEntity(m, c)).cache();
    return orderEntity.flatMap(oe -> dbClient.sql("""
          INSERT INTO ecomm.orders (address_id, card_id, customer_id, order_date,
          total, status) VALUES ($1, $2, $3, $4, $5, $6)
        """)
        .bind("$1", Parameter.fromOrEmpty(oe.getAddressId(), UUID.class))
        .bind("$2", Parameter.fromOrEmpty(oe.getCardId(), UUID.class))
        .bind("$3", Parameter.fromOrEmpty(oe.getCustomerId(), UUID.class))
        .bind("$4",
            OffsetDateTime.ofInstant(oe.getOrderDate().toInstant(), ZoneId.of("Z")).truncatedTo(ChronoUnit.MICROS))
        .bind("$5", oe.getTotal())
        .bind("$6", StatusEnum.CREATED.getValue())
        .map(new OrderMapper()::apply).one())
        .then(orderEntity.flatMap(x -> template.selectOne(
            query(where("customer_id").is(x.getCustomerId())
                .and("order_date")
                .greaterThanOrEquals(
                    OffsetDateTime.ofInstant(x.getOrderDate().toInstant(), ZoneId.of("X"))
                        .truncatedTo(ChronoUnit.MICROS))),
            OrderEntity.class)
            .map(t -> x.setId(t.getId()).setStatus(t.getStatus()))));

  }

  @Override
  public Mono<OrderEntity> updateMapping(OrderEntity orderEntity) {
    return oiRepo.saveAll(orderEntity.getItems().stream()
        .map(i -> new OrderItemEntity().setOrderId(orderEntity.getId())
            .setItemId(i.getId()))
        .toList())
        .then(
            itemRepository.deleteCartitemJoinByid(orderEntity.getItems().stream()
                .map(i -> i.getId()).toList(), orderEntity.getCartId()))
        .then(Mono.just(orderEntity));
  }
}

class OrderMapper implements BiFunction<Row, Object, OrderEntity> {

  @Override
  public OrderEntity apply(Row row, Object o) {
    OrderEntity oe = new OrderEntity();
    return oe.setId(row.get("id", UUID.class))
        .setCustomerId(row.get("customer_id", UUID.class))
        .setAddressId(row.get("address_id", UUID.class))
        .setCardId(row.get("card_id", UUID.class))
        .setOrderDate(Timestamp.from(
            ZonedDateTime.of((LocalDateTime) row.get("order_date"), ZoneId.of("Z")).toInstant()))
        .setTotal(row.get("total", BigDecimal.class))
        .setPaymentId(row.get("payment_id", UUID.class))
        .setShipmentId(row.get("shipment_id", UUID.class))
        .setStatus(StatusEnum.fromValue(row.get("status", String.class)));
  }
}
