package com.packt.modern.api.service;

import com.packt.modern.api.generated.types.Product;
import com.packt.modern.api.generated.types.ProductCriteria;
import org.reactivestreams.Publisher;

import java.util.List;

public interface ProductService {
  Product getProduct(String id);

  List<Product> getProducts(ProductCriteria criteria);

  Product addQuantity(String productId, int qty);

  Publisher<Product> getProductPublisher();
}
