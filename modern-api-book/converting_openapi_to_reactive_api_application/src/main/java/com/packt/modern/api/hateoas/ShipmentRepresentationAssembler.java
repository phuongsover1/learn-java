package com.packt.modern.api.hateoas;

import com.packt.modern.api.controllers.ShipmentController;
import com.packt.modern.api.entity.ShipmentEntity;
import com.packt.modern.api.model.Shipment;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ShipmentRepresentationAssembler extends RepresentationModelAssemblerSupport<ShipmentEntity, Shipment> {
  public ShipmentRepresentationAssembler() {
    super(ShipmentController.class, Shipment.class);
  }

  /**
   * Converts the shipment entity to resource
   * @param entity
   * @return shipment resource
   */
  @Override
  public Shipment toModel(ShipmentEntity entity) {
    Shipment resource = createModelWithId(entity.getId(), entity);
    BeanUtils.copyProperties(entity, resource);
    resource.setId(entity.getId().toString());

    resource.add(linkTo(methodOn(ShipmentController.class).getShipmentByOrderId(entity.getId().toString())).withRel("byOrderId"));
    return resource;
  }

  /**
   * Converts list of shipment to resources
   * @param entities
   * @return shipment resources
   */
  public List<Shipment> toModelList(Iterable<ShipmentEntity> entities) {
    if (Objects.isNull(entities)) {return List.of();}
    return StreamSupport.stream(
        entities.spliterator(), false
    )
        .map(this::toModel)
        .toList();
  }
}
