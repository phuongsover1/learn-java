package com.packt.modern.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import com.packt.modern.api.datafetchers.ProductDatafetcher;
import com.packt.modern.api.generated.types.Product;
import com.packt.modern.api.generated.types.Tag;
import com.packt.modern.api.generated.types.TagInput;
import com.packt.modern.api.repository.InMemRepository;
import com.packt.modern.api.scalar.BigDecimalScalar;
import com.packt.modern.api.service.ProductService;
import com.packt.modern.api.service.TagService;
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
    Product product =
        Product.newBuilder()
            .id("any")
            .name("mock title")
            .description("mock description")
            .price(BigDecimal.valueOf(20.20))
            .count(100)
            .tags(tags)
            .build();

    given(productService.getProduct("any")).willReturn(product);
    tags.add(Tag.newBuilder().id("tag2").name("addTags").build());
    product.setTags(tags);
    given(tagService.addTags("any", List.of(TagInput.newBuilder().name("addTags").build())))
            .willAnswer(invocationOnMock -> product);
  }

  @Test
  @DisplayName("Verify JSON returned by the query 'product'")
  public void product() {
    String name = dgsQueryExecutor
            .executeAndExtractJsonPath("{ product(id: \"any\") { name }}", "data.product.name");
    assertThat(name).contains("mock title");
  }
}
