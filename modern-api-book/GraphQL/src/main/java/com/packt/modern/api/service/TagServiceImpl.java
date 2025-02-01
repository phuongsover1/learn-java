package com.packt.modern.api.service;

import com.packt.modern.api.generated.types.Product;
import com.packt.modern.api.generated.types.Tag;
import com.packt.modern.api.generated.types.TagInput;
import com.packt.modern.api.repository.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TagServiceImpl implements TagService {
    private final Repository repository;

    public TagServiceImpl(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Map<String, List<Tag>> getTags(List<String> productIds) {
        return repository.getProductTagMapping(productIds);
    }

    @Override
    public Product addTags(String productId, List<TagInput> tags) {
        return repository.addTags(productId, tags);
    }

}
