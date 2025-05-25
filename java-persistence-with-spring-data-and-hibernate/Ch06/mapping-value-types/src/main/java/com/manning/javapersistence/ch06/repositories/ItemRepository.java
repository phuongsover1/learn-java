package com.manning.javapersistence.ch06.repositories;

import com.manning.javapersistence.ch06.model.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Long> {
}
