package com.learn.hibernate.ch02.repository;

import com.learn.hibernate.ch02.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {}
