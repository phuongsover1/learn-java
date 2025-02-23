package com.packt.modern.api.repository;

import com.packt.modern.api.generated.types.Product;
import com.packt.modern.api.generated.types.Tag;
import com.packt.modern.api.generated.types.TagInput;
import com.packt.modern.api.service.TagService;
import net.datafaker.Faker;
import org.apache.logging.log4j.util.Strings;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

@org.springframework.stereotype.Repository
public class InMemRepository implements Repository {
  private static final Map<String, Product> products = new ConcurrentHashMap<>();
  private static final Map<String, Tag> tags = new ConcurrentHashMap<>();
  private final Logger LOG = LoggerFactory.getLogger(getClass());

  private FluxSink<Product> productsStream;
  private ConnectableFlux<Product> productPublisher;

  public InMemRepository() {
    // Seed data for development purpose, real application must use database
    Faker faker = new Faker();
    IntStream.range(0, faker.number().numberBetween(20, 50))
        .forEach(
            number -> {
              String tag = faker.book().genre();
              tags.put(tag, Tag.newBuilder().id(UUID.randomUUID().toString()).name(tag).build());
            });
    IntStream.range(0, faker.number().numberBetween(4, 20))
        .forEach(
            number -> {
              String id = String.format("a1s2d3f4-%d", number);
              String title = faker.book().title();
              List<Tag> tags =
                  InMemRepository.tags.entrySet().stream()
                      .filter(t -> t.getKey().startsWith(faker.book().genre().substring(0, 1)))
                      .map(Map.Entry::getValue)
                      .collect(Collectors.toList());
              if (tags.isEmpty()) {
                tags.add(InMemRepository.tags.entrySet().stream().findAny().get().getValue());
              }

              Product product =
                  Product.newBuilder()
                      .id(id)
                      .name(title)
                      .description(faker.lorem().sentence())
                      .count(faker.number().numberBetween(10, 100))
                      .price(BigDecimal.valueOf(faker.number().randomDigitNotZero()))
                      .imageUrl(String.format("/images/%s.jpeg", title.replace(" ", "")))
                      .tags(tags)
                      .build();
              products.put(id, product);
            });
      Flux<Product> publisher = Flux.create(emitter -> {
          productsStream = emitter;
      });
      productPublisher = publisher.publish();
      productPublisher.connect();
  }

  @Override
  public Product getProduct(String productId) {
    if (Strings.isBlank(productId)) throw new RuntimeException("Invalid Product ID");
    Product product = products.get(productId);
    if (Objects.isNull(product)) throw new RuntimeException("Product not found.");
    return product;
  }

  @Override
  public List<Product> getProducts() {
    return products.values().stream().toList();
  }

  @Override
  public Map<String, List<Tag>> getProductTagMapping(List<String> productIds) {
    return products.entrySet().stream().filter(e -> productIds.contains(e.getKey()))
            .collect(toMap(Map.Entry::getKey, e -> e.getValue().getTags()));
  }

  @Override
  public Product addTags(String productId, List<TagInput> tags) {
    if (Objects.isNull(productId)) throw new RuntimeException("Invalid Product ID");
    if (Strings.isBlank(productId)) throw new RuntimeException("Product ID must not be empty");
    Product product = products.get(productId);
    if (product == null) throw new RuntimeException("Product not found.");
    if (tags == null || tags.isEmpty()) return product;

    List<String> newTags = tags.stream().map(TagInput::getName).collect(Collectors.toList());
    List<String> oldTags =
        product.getTags().stream().map(Tag::getName).collect(Collectors.toList());
    newTags.stream()
        .forEach(
            nt -> {
              if (!oldTags.contains(nt)) {
                Tag newTag = Tag.newBuilder().id(UUID.randomUUID().toString()).name(nt).build();
                InMemRepository.tags.put(newTag.getName(), newTag);
                product.getTags().add(newTag);
              }
            });
    products.put(product.getId(), product);
    return product;
  }

  @Override
  public Product addQuantity(String productId, int quantity) {
    if (productId == null || Strings.isBlank(productId))
      throw new RuntimeException("Invalid Product ID");
    Product product = products.get(productId);
    if (product == null) throw new RuntimeException("Product not found.");
    product.setCount(product.getCount() + quantity);
    products.put(productId, product);
    productsStream.next(product);
    return product;
  }

  @Override
  public Publisher<Product> getProductPublisher() {
      return productPublisher;
  }
}
