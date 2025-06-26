package com.manning.javapersistence.springdatajpa.repositories;

import org.springframework.data.repository.CrudRepository;

import com.manning.javapersistence.springdatajpa.model.Item;

public interface ItemRepository extends CrudRepository<Item, Long> {

}
