package com.packt.modern.api.repository;

import com.packt.modern.api.generated.types.Product;

import java.util.List;

public interface Repository {
    Product getProduct(String productId);
    List<Product> getProducts();
}
