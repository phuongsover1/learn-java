package com.packt.modern.api.service;

import com.packt.modern.api.generated.types.Product;
import com.packt.modern.api.generated.types.ProductCriteria;
import com.packt.modern.api.repository.Repository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Flow;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
  private final Repository repository;

  public ProductServiceImpl(Repository repository) {
    this.repository = repository;
  }

  @Override
  public Product getProduct(String id) {
    return this.repository.getProduct(id);
  }

  @Override
  public List<Product> getProducts(ProductCriteria criteria) {
    List<Predicate<Product>> predicates = new ArrayList<>(2);
    if (!Objects.isNull(criteria)) {
      if (Strings.isNotBlank(criteria.getName())) {
        Predicate<Product> namePredicate =
            product -> product.getName().contains(criteria.getName());
        predicates.add(namePredicate);
      }
      if (Objects.isNull(criteria.getTags()) && !criteria.getTags().isEmpty()) {
        List<String> tagCriteriaName =
            criteria.getTags().stream().map(tag -> tag.getName()).collect(Collectors.toList());
        Predicate<Product> tagPredicate =
            product ->
                product.getTags().stream()
                        .filter(productTag -> tagCriteriaName.contains(productTag.getName()))
                        .count()
                    > 0;
        predicates.add(tagPredicate);
      }
    }
    if (predicates.isEmpty()) {

      return repository.getProducts();
    }
    return repository.getProducts().stream()
        .filter(p -> predicates.stream().allMatch(pre -> pre.test(p)))
        .toList();
  }

  @Override
  public Product addQuantity(String productId, int qty) {
    return repository.addQuantity(productId, qty);
  }

  @Override
  public Flow.Publisher<Product> getProductPublisher() {
    return null;
  }
}
