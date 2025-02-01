package com.packt.modern.api.service;

import com.packt.modern.api.generated.types.Product;
import com.packt.modern.api.generated.types.ProductCriteria;

import java.util.List;
import java.util.concurrent.Flow;

public interface ProductService {
    Product getProduct(String id);
    List<Product> getProducts(ProductCriteria criteria);
    Product addQuantity(String productId, int qty);
    Flow.Publisher<Product> getProductPublisher();

}
