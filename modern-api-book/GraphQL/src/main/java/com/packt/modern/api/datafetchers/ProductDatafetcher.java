package com.packt.modern.api.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import com.packt.modern.api.generated.DgsConstants;
import com.packt.modern.api.generated.types.Product;
import com.packt.modern.api.service.ProductService;
import org.apache.logging.log4j.util.Strings;

@DgsComponent
public class ProductDatafetcher {
  private final ProductService productService;

  public ProductDatafetcher(ProductService productService) {
    this.productService = productService;
  }

  @DgsData(parentType = DgsConstants.QUERY_TYPE, field = DgsConstants.QUERY.Product)
  public Product getProduct(@InputArgument("id") String id) {
    if (Strings.isBlank(id)) {
      new RuntimeException("Product id is required");
    }
    return productService.getProduct(id);
  }
}
