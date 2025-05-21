package com.manning.javapersistence.springdatajpa.repositories;

import com.manning.javapersistence.springdatajpa.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
