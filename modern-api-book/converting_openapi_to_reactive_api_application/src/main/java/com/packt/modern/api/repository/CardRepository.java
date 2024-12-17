package com.packt.modern.api.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.packt.modern.api.entity.CardEntity;

import reactor.core.publisher.Mono;



public interface CardRepository extends ReactiveCrudRepository<CardEntity, UUID> {
    Mono<CardEntity> findByUserId(UUID userId);
}

