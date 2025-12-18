package com.manning.persistence.ch07.repositories;

import com.manning.persistence.ch07.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
