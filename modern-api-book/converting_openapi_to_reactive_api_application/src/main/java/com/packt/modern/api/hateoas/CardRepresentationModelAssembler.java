package com.packt.modern.api.hateoas;

import com.packt.modern.api.controllers.CardController;
import com.packt.modern.api.entity.CardEntity;
import com.packt.modern.api.model.Card;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CardRepresentationModelAssembler extends RepresentationModelAssemblerSupport<CardEntity, Card> {
  public CardRepresentationModelAssembler() {
    super(CardController.class, Card.class);
  }

  @Override
  public Card toModel(CardEntity entity) {
    String cId = Objects.nonNull(entity.getId()) ? entity.getId().toString() : null;
    String uId = Objects.nonNull(entity.getUser().getId()) ? entity.getUser().getId().toString() : null;

    Card resource = new Card();
    BeanUtils.copyProperties(entity, resource);
    resource.id(cId).userId(uId);

    try {
      resource.add(linkTo(methodOn(CardController.class).getCardById(cId)).withSelfRel());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return resource;
  }

  public List<Card> toModelList(Iterable<CardEntity> entities) {
    if (Objects.isNull(entities)) {
      return List.of();
    }
    return StreamSupport.stream(
            entities.spliterator(), false
        ).map(this::toModel)
        .toList();
  }
}
