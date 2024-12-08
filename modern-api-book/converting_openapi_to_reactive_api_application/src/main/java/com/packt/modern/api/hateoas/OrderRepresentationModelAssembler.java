package com.packt.modern.api.hateoas;

import com.packt.modern.api.controllers.OrderController;
import com.packt.modern.api.entity.OrderEntity;
import com.packt.modern.api.model.Order;
import com.packt.modern.api.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class OrderRepresentationModelAssembler extends RepresentationModelAssemblerSupport<OrderEntity, Order> {
  private static final Logger log = LoggerFactory.getLogger(OrderRepresentationModelAssembler.class);
  private final UserRepresentationModelAssembler uAssembler;
  private final AddressRepresentationModelAssembler aAssembler;
  private final CardRepresentationModelAssembler cAssembler;
  private final ShipmentRepresentationAssembler sAssembler;
  private final ItemService itemService;

  public OrderRepresentationModelAssembler(UserRepresentationModelAssembler uAssembler, AddressRepresentationModelAssembler aAssembler, CardRepresentationModelAssembler cAssembler, ShipmentRepresentationAssembler sAssembler, ItemService itemService) {
    super(OrderController.class, Order.class);
    this.uAssembler = uAssembler;
    this.aAssembler = aAssembler;
    this.cAssembler = cAssembler;
    this.sAssembler = sAssembler;
    this.itemService = itemService;
  }

  /**
   * Coverts order entity to order resource
   *
   * @param entity
   * @return order resource
   */
  @Override
  public Order toModel(OrderEntity entity) {
    log.info("\n\nentity = {}", entity);
    Order resource = createModelWithId(entity.getId(), entity);
    BeanUtils.copyProperties(entity, resource);
    resource.id(entity.getId().toString())
        .customer(uAssembler.toModel(entity.getUserEntity()))
        .address(aAssembler.toModel(entity.getAddressEntity()))
        .card(cAssembler.toModel(entity.getCardEntity()))
        .items(itemService.toModelList(entity.getItems()))
        .date(entity.getOrderDate().toInstant().atOffset(ZoneOffset.UTC));
    log.info("\nResource = {}", resource);

    resource.add(linkTo(methodOn(OrderController.class).getByOrderId(entity.getId().toString())).withSelfRel());
    return resource;
  }

  /**
   * Converts list of order entities to resources
   * @param entities
   * @return order resources
   */
  public List<Order> toModelList(Iterable<OrderEntity> entities) {
    if (Objects.isNull(entities)) {
      return List.of();
    }
    return StreamSupport.stream(
            entities.spliterator(), false
        ).map(this::toModel)
        .toList();
  }
}
