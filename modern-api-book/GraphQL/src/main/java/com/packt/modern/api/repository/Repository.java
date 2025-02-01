package com.packt.modern.api.repository;

import com.packt.modern.api.generated.types.Product;
import com.packt.modern.api.generated.types.Tag;

import java.util.List;
import java.util.Map;

public interface Repository {
    Product getProduct(String productId);
    List<Product> getProducts();
    Map<String, List<Tag>> getProductTagMapping(List<String> productIds);
}
