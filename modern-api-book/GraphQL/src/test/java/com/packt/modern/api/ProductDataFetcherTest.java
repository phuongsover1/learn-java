package com.packt.modern.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import com.packt.modern.api.datafetchers.ProductDatafetcher;
import com.packt.modern.api.dataloaders.TagDataLoader;
import com.packt.modern.api.dataloaders.TagsDataLoaderWithContext;
import com.packt.modern.api.generated.client.ProductGraphQLQuery;
import com.packt.modern.api.generated.client.ProductProjectionRoot;
import com.packt.modern.api.generated.types.Product;
import com.packt.modern.api.generated.types.Tag;
import com.packt.modern.api.generated.types.TagInput;
import com.packt.modern.api.repository.InMemRepository;
import com.packt.modern.api.scalar.BigDecimalScalar;
import com.packt.modern.api.service.ProductService;
import com.packt.modern.api.service.TagService;
import graphql.ExecutionResult;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(
    classes = {DgsAutoConfiguration.class, ProductDatafetcher.class, BigDecimalScalar.class})
public class ProductDataFetcherTest {
  private final InMemRepository repo = new InMemRepository();
  private final int TEN = 10;

  @Autowired private DgsQueryExecutor dgsQueryExecutor;
  @MockBean private ProductService productService;
  @MockBean private TagService tagService;

  @BeforeEach
  public void beforeEach() {
    List<Tag> tags = new ArrayList<>();
    tags.add(Tag.newBuilder().id("tag1").name("Tag 1").build());
    Product product = Product.newBuilder().id("any").name("mock title")
            .description("mock description").price(BigDecimal.valueOf(20.20)).count(100)
            .tags(tags).build();
    given(productService.getProduct("any")).willReturn(product);
    tags.add(Tag.newBuilder().id("tag2").name("addTags").build());
    product.setTags(tags);
    given(tagService.addTags("any", List.of(TagInput.newBuilder().name("addTags").build())))
            .willAnswer(invocation -> product);
  }

  @Test
  @DisplayName("Verify JSON returned by the query 'product'")
  public void product() {
    String name =
        dgsQueryExecutor.executeAndExtractJsonPath(
            "{ product(id: \"any\") { name }}", "data.product.name");
    assertThat(name).contains("mock title");
  }

  @Test
  @DisplayName("Verify exception to query product - invalid ID")
  public void productWithException() {
    // given
    given(productService.getProduct("any")).willThrow(new RuntimeException("Invalid Product ID"));

    // when
    ExecutionResult res = dgsQueryExecutor.execute("{ product(id: \"any\") { name }}");
    // then
    verify(productService, times(1)).getProduct("any");
    assertThat(res.getErrors()).isNotEmpty();
    assertThat(res.getErrors().get(0).getMessage())
        .isEqualTo("java.lang.RuntimeException: Invalid Product ID");
  }

  @Test
  @DisplayName("Verify JSON using GraphQLQueryRequest")
  void productsWithQueryAPI() {
    GraphQLQueryRequest gqlRequest = new GraphQLQueryRequest(
            ProductGraphQLQuery.newRequest().id("any").build(),
            new ProductProjectionRoot().id().name()
    );

    String id = dgsQueryExecutor.executeAndExtractJsonPath(
            gqlRequest.serialize(), "data.product.id"
    );
    String name = dgsQueryExecutor.executeAndExtractJsonPath(
            gqlRequest.serialize(), "data.product.name"
    );

    assertThat(id).contains("any");
    assertThat(name).contains("mock title");
  }

  @Test
  @DisplayName("Verify Tags returned by the query 'product'")
  void productsWithTags() {
    GraphQLQueryRequest gqlRequest = new GraphQLQueryRequest(
            ProductGraphQLQuery.newRequest().id("any").build(),
            new ProductProjectionRoot().id().name().tags().id().name()
    );

    Product product = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
            gqlRequest.serialize(),
            "data.product",
            new TypeRef<>() {}
    );
    assertThat(product.getId()).isEqualTo("any");
    assertThat(product.getName()).isEqualTo("mock title");
    assertThat(product.getTags().size()).isEqualTo(2);
    assertThat(product.getTags().get(0).getId()).isEqualTo("tag1");
    assertThat(product.getTags().get(0).getName()).isEqualTo("Tag 1");
  }
}
