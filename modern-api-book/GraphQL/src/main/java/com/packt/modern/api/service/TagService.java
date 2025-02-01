package com.packt.modern.api.service;

import com.packt.modern.api.generated.types.Product;
import com.packt.modern.api.generated.types.Tag;
import com.packt.modern.api.generated.types.TagInput;

import java.util.List;
import java.util.Map;

public interface TagService {
    Map<String, List<Tag>> getTags(List<String> productIds);
    Product addTags(String productId, List<TagInput> tags);
}
