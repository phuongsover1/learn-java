package com.packt.modern.api.hateoas;

import com.packt.modern.api.controllers.CustomerController;
import com.packt.modern.api.entity.UserEntity;
import com.packt.modern.api.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserRepresentationModelAssembler extends RepresentationModelAssemblerSupport<UserEntity, User> {

  public UserRepresentationModelAssembler() {
    super(CustomerController.class, User.class);
  }
  @Override
  public User toModel(UserEntity entity) {
    User resource = createModelWithId(entity.getId(), entity);
    BeanUtils.copyProperties(entity, resource);

    resource.setId(entity.getId().toString());

    resource.add(
        linkTo(methodOn(CustomerController.class).getCustomerById(entity.getId().toString())).withSelfRel()
    );
    resource.add(
        linkTo(methodOn(CustomerController.class).getAllCustomers()).withRel("customers")
    );
    resource.add(
        linkTo(methodOn(CustomerController.class).getAddressesByCustomerId(entity.getId().toString())).withRel("self_addresses")
    );
    return resource;
  }

  public List<User> toModelList(Iterable<UserEntity> entities) {
    if (Objects.isNull(entities)) {return List.of();}
    return StreamSupport.stream(
        entities.spliterator(), false
    )
        .map(this::toModel)
        .toList();
  }
}
