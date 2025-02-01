package com.packt.modern.api.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.InputArgument;
import com.packt.modern.api.dataloaders.TagsDataLoaderWithContext;
import com.packt.modern.api.generated.DgsConstants;
import com.packt.modern.api.generated.types.Product;
import com.packt.modern.api.generated.types.ProductCriteria;
import com.packt.modern.api.service.ProductService;
import org.apache.logging.log4j.util.Strings;
import org.dataloader.DataLoader;
import org.springframework.core.metrics.StartupStep;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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

  @DgsData(parentType = DgsConstants.QUERY_TYPE, field = DgsConstants.QUERY.Products)
  public List<Product> getProducts(@InputArgument("filter") ProductCriteria criteria) {
    return productService.getProducts(criteria);
  }

  @DgsData(
          parentType = DgsConstants.PRODUCT.TYPE_NAME,
          field = DgsConstants.PRODUCT.Tags
  )
  public CompletableFuture<List<StartupStep.Tags>> tags (DgsDataFetchingEnvironment env) {
    DataLoader<String, List<StartupStep.Tags>> tagsDataLoader = env.getDataLoader(TagsDataLoaderWithContext.class);
    Product product = env.getSource();
    return tagsDataLoader.load(product.getId());
  }
}
