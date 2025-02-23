package com.packt.modern.api.datafetchers;

import com.netflix.graphql.dgs.*;
import com.packt.modern.api.dataloaders.TagsDataLoaderWithContext;
import com.packt.modern.api.generated.DgsConstants;
import com.packt.modern.api.generated.types.Product;
import com.packt.modern.api.generated.types.ProductCriteria;
import com.packt.modern.api.generated.types.TagInput;
import com.packt.modern.api.service.ProductService;
import com.packt.modern.api.service.TagService;
import org.apache.logging.log4j.util.Strings;
import org.dataloader.DataLoader;
import org.reactivestreams.Publisher;
import org.springframework.core.metrics.StartupStep;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
public class ProductDatafetcher {
  private final ProductService productService;
  private final TagService tagService;

  public ProductDatafetcher(ProductService productService, TagService tagService) {
    this.productService = productService;
    this.tagService = tagService;
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

 // Disable when testing
//  @DgsData(parentType = DgsConstants.PRODUCT.TYPE_NAME, field = DgsConstants.PRODUCT.Tags)
//  public CompletableFuture<List<StartupStep.Tags>> tags(DgsDataFetchingEnvironment env) {
//    DataLoader<String, List<StartupStep.Tags>> tagsDataLoader =
//        env.getDataLoader(TagsDataLoaderWithContext.class);
//    Product product = env.getSource();
//    return tagsDataLoader.load(product.getId());
//  }

  @DgsMutation(field = DgsConstants.MUTATION.AddTag)
  public Product addTags(
      @InputArgument("productId") String productId,
      @InputArgument(value = "tags", collectionType = TagInput.class) List<TagInput> tags) {
    return tagService.addTags(productId, tags);
  }

  @DgsMutation(field = DgsConstants.MUTATION.AddQuantity)
  public Product addQuantity(
      @InputArgument("productId") String productId, @InputArgument("quantity") int qty) {
    return productService.addQuantity(productId, qty);
  }

  @DgsSubscription(field = DgsConstants.SUBSCRIPTION.QuantityChanged)
  public Publisher<Product> quantityChanged() {
    return productService.getProductPublisher();
  }
}
