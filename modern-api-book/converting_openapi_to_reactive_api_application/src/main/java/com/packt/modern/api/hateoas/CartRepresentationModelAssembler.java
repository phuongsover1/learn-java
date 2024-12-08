package com.packt.modern.api.hateoas;

import com.packt.modern.api.controllers.CartsController;
import com.packt.modern.api.entity.CartEntity;
import com.packt.modern.api.model.Cart;
import com.packt.modern.api.service.ItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class CartRepresentationModelAssembler extends RepresentationModelAssemblerSupport<CartEntity, Cart> {
  private final ItemService iService;

  public CartRepresentationModelAssembler(ItemService iService) {
    super(CartsController.class, Cart.class);
    this.iService = iService;
  }

  @Override
  public Cart toModel(CartEntity entity) {
    String uid = Objects.nonNull(entity.getUser()) ?
        entity.getUser().getId().toString() : null;
    String cid = Objects.nonNull(entity.getId()) ?
        entity.getId().toString() : null;
    Cart resource = new Cart();
    BeanUtils.copyProperties(entity, resource);
    resource.id(cid).customerId(uid).items(iService.toModelList(entity.getItems()));
    try {
      resource.add(linkTo(methodOn(CartsController.class).getCartByCustomerId(uid)).withSelfRel());
      resource.add(linkTo(methodOn(CartsController.class).getCartItemsByCustomerId(uid)).withRel("cart-items"));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return resource;
  }

  public List<Cart> toListModel(Iterable<CartEntity> entities) {
    if (Objects.isNull(entities)) {
      return List.of();
    }
    return StreamSupport.stream(
            entities.spliterator(), false)
        .map(this::toModel)
        .toList();
  }


}
