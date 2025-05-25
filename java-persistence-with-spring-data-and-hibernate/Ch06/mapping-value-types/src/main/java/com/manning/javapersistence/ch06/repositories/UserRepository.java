package com.manning.javapersistence.ch06.repositories;

import com.manning.javapersistence.ch06.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
