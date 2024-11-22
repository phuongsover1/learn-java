package com.packt.modern.api.repository;

import com.packt.modern.api.model.NewOrder;
import com.packt.modern.api.model.Order;
import com.packt.modern.api.entity.CartEntity;
import com.packt.modern.api.entity.ItemEntity;
import com.packt.modern.api.entity.OrderEntity;
import com.packt.modern.api.entity.OrderItemEntity;
import com.packt.modern.api.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Repository
@Transactional
public class OrderRepositoryImpl implements OrderRepositoryExt {
  private final ItemRepository itemRepo;
  private final CartRepository cRepo;
  private final OrderItemRepository oiRepo;
  @PersistenceContext
  private EntityManager em;

  public OrderRepositoryImpl(EntityManager em, CartRepository cRepo, ItemRepository itemRepo, OrderItemRepository oiRepo) {
    this.em = em;
    this.itemRepo = itemRepo;
    this.cRepo = cRepo;
    this.oiRepo = oiRepo;
  }

  @Override
  public Optional<OrderEntity> insert(NewOrder m) {
    Iterable<ItemEntity> dbItems = itemRepo.findByCustomerId(m.getCustomerId());
    List<ItemEntity> items = StreamSupport.stream(dbItems.spliterator(), false).toList();
    if (items.isEmpty()) {
      throw new ResourceNotFoundException(String.format("There is not item found in customer's (ID: %s) cart", m.getCustomerId()));
    }
    BigDecimal total = BigDecimal.ZERO;
    for (ItemEntity item : items) {
      total = (BigDecimal.valueOf(item.getQuantity())
          .multiply(item.getPrice())
          .add(total));
    }
    Timestamp orderDate = Timestamp.from(Instant.now());
    em.createNativeQuery("""
            INSERT INTO ecomm.orders (address_id, card_id, customer_id, order_date, total, status)\s
            VALUES\s
            (?, ?, ?, ?, ?, ?)
            """)
        .setParameter(1, m.getAddress().getId())
        .setParameter(2, m.getCard().getId())
        .setParameter(3, m.getCustomerId())
        .setParameter(4, orderDate)
        .setParameter(5, total)
        .setParameter(6, Order.StatusEnum.CREATED.getValue())
        .executeUpdate();
    Optional<CartEntity> oCart = cRepo.findByCustomerId(UUID.fromString(m.getCustomerId()));
    CartEntity cart = oCart.orElseThrow(() -> new ResourceNotFoundException(String.format("Cart not found for given customer (ID: %s)", m.getCustomerId())));
    itemRepo.deleteCartitemJoinByid(cart.getItems().stream().map(item -> item.getId()).toList(),
        cart.getId());

    OrderEntity entity = (OrderEntity) em.createNativeQuery(
            """
                SELECT o.* FROM ecomm.orders o WHERE o.customer_id = ? AND o.order_date >= ?
                """)
        .setParameter(1, m.getCustomerId())
        .setParameter(2, OffsetDateTime.ofInstant(orderDate.toInstant(), ZoneId.of("Z")).truncatedTo(ChronoUnit.MICROS))
        .getSingleResult();
    oiRepo.saveAll(cart.getItems().stream()
        .map(i -> new OrderItemEntity()
            .setOrderId(entity.getId())
            .setItemId(i.getId())
        )
        .toList());
    return Optional.of(entity);


  }
}
