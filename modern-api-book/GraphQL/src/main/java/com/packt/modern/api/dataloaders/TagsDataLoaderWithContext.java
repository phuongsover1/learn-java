package com.packt.modern.api.dataloaders;

import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataLoader;
import com.packt.modern.api.generated.types.Tag;
import com.packt.modern.api.service.TagService;
import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.MappedBatchLoaderWithContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader(name = "tagsWithContext")
public class TagsDataLoaderWithContext implements MappedBatchLoaderWithContext<String, List<Tag>> {
  private final TagService tagService;

  public TagsDataLoaderWithContext(TagService tagService) {
    this.tagService = tagService;
  }

  @Override
  public CompletionStage<Map<String, List<Tag>>> load(
      Set<String> keys, BatchLoaderEnvironment batchLoaderEnvironment) {
    return CompletableFuture.supplyAsync(() -> tagService.getTags(new ArrayList<>(keys)));
  }
}
