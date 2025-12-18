package com.manning.persistence.ch07.repositories;

import com.manning.persistence.ch07.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {}
