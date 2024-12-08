package com.packt.modern.api.hateoas;

import com.packt.modern.api.controllers.ProductController;
import com.packt.modern.api.entity.ProductEntity;
import com.packt.modern.api.model.Product;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductRepresentationAssembler extends RepresentationModelAssemblerSupport<ProductEntity,Product> {
  public ProductRepresentationAssembler() {
    super(ProductController.class, Product.class);
  }

  /**
   * Converts the Product entity to resource
   * @param entity
   * @return Product resource
   */
  @Override
  public Product toModel(ProductEntity entity) {
    Product resource = createModelWithId(entity.getId(), entity);
    BeanUtils.copyProperties(entity, resource);
    resource.setId(entity.getId().toString());

    resource.add(linkTo(methodOn(ProductController.class).getProduct(entity.getId().toString())).withSelfRel());
    resource.add(linkTo(methodOn(ProductController.class).queryProducts(null, null, 1, 10)).withRel("products"));
    return resource;
  }

  /**
   * Convert List of product entity to list of product resource
   * @param entities
   * @return product resources
   */
  public List<Product> toModelList(Iterable<ProductEntity> entities) {
    if (Objects.isNull(entities)) {return List.of();}
    return StreamSupport.stream(
        entities.spliterator(), false
    ).map(this::toModel)
        .toList();
  }
}
