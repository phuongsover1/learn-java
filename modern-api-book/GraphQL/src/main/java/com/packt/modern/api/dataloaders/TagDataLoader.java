package com.packt.modern.api.dataloaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import com.packt.modern.api.generated.types.Tag;
import com.packt.modern.api.service.TagService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import org.dataloader.MappedBatchLoader;

@DgsDataLoader(name = "tags")
public class TagDataLoader implements MappedBatchLoader<String, List<Tag>> {
  private final TagService tagService;

  public TagDataLoader(TagService tagService) {
    this.tagService = tagService;
  }

  @Override
  public CompletionStage<Map<String, List<Tag>>> load(Set<String> set) {
    return CompletableFuture.supplyAsync(() -> tagService.getTags(new ArrayList<>(set)));
  }
}
