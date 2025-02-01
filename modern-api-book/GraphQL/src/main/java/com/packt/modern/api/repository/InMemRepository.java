package com.packt.modern.api.repository;

import com.packt.modern.api.generated.types.Product;
import com.packt.modern.api.generated.types.Tag;
import net.datafaker.Faker;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

@org.springframework.stereotype.Repository
public class InMemRepository implements Repository {
  private static final Map<String, Product> products = new ConcurrentHashMap<>();
  private static final Map<String, Tag> tags = new ConcurrentHashMap<>();
  private final Logger LOG = LoggerFactory.getLogger(getClass());

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
                      .toList();
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
}
