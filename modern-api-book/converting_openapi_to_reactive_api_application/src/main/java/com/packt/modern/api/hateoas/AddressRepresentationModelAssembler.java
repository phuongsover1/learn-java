package com.packt.modern.api.hateoas;

import com.packt.modern.api.controllers.AddressController;
import com.packt.modern.api.entity.AddressEntity;
import com.packt.modern.api.model.Address;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AddressRepresentationModelAssembler extends RepresentationModelAssemblerSupport<AddressEntity, Address> {

  public AddressRepresentationModelAssembler() {
    super(AddressController.class, Address.class);
  }

  @Override
  public Address toModel(AddressEntity entity) {
    String aId = Objects.nonNull(entity.getId()) ? entity.getId().toString() : null;
    Address resource = new Address();
    BeanUtils.copyProperties(entity, resource);
    resource.setId(aId);

    resource.add(linkTo(methodOn(AddressController.class).getAddressesById(aId)).withSelfRel());
    return resource;
  }

  public List<Address> toModelList(Iterable<AddressEntity> entities) {
    if (Objects.isNull(entities)) {
      return List.of();
    }
    return StreamSupport.stream(
        entities.spliterator(), false
    ).map(this::toModel).toList();
  }
}
