package com.packt.modern.api.hateoas;

import com.packt.modern.api.controllers.PaymentController;
import com.packt.modern.api.entity.PaymentEntity;
import com.packt.modern.api.model.Payment;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PaymentRepresentationAssembler extends RepresentationModelAssemblerSupport<PaymentEntity, Payment> {
  public PaymentRepresentationAssembler() {
    super(PaymentController.class, Payment.class);
  }

  /**
   * Coverts the Payment entity to resource
   *
   * @param entity
   * @return payment resource
   */
  @Override
  public Payment toModel(PaymentEntity entity) {
    Payment resource = createModelWithId(entity.getId(), entity);
    BeanUtils.copyProperties(entity, resource);
    resource.setId(entity.getId().toString());

    resource.add(linkTo(methodOn(PaymentController.class).getOrdersPaymentAuthorization(entity.getId().toString())).withSelfRel());
    return resource;
  }

  /**
   * Coverts the collection of Product entities to list of resources
   * @param entities
   * @return List of payment resource
   */
  public List<Payment> toModelList(Iterable<PaymentEntity> entities) {
    if (Objects.isNull(entities)) {
      return List.of();
    }
    return StreamSupport.stream(
            entities.spliterator(), false
        ).map(this::toModel)
        .toList();
  }
}
