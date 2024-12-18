package com.packt.modern.api.controllers;

import com.packt.modern.api.CardApi;
import com.packt.modern.api.hateoas.CardRepresentationModelAssembler;
import com.packt.modern.api.model.AddCardReq;
import com.packt.modern.api.model.Card;
import com.packt.modern.api.service.CardService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
public class CardController implements CardApi {
  private final CardService cService;
  private final CardRepresentationModelAssembler assembler;

  public CardController(CardService cService, CardRepresentationModelAssembler assembler) {
    this.cService = cService;
    this.assembler = assembler;
  }

  @Override
  public Mono<ResponseEntity<Void>> deleteCardById(String id, ServerWebExchange exchange) {
    cService.deleteCardById(id).subscribeOn(Schedulers.boundedElastic());
    return Mono.just(accepted().build());
  }

  @Override
  public Mono<ResponseEntity<Card>> getCardById(String id, ServerWebExchange exchange) {
    return cService.getCardById(id)
    .map(c -> ok(assembler.entityToModel(c, exchange)))
    .defaultIfEmpty(notFound().build());
  }

  @Override
  public Mono<ResponseEntity<Flux<Card>>> getAllCards(ServerWebExchange exchange) {
    return Mono.just(ok(assembler.tolistModel(cService.getAllCards(), exchange)));
  }

  @Override
  public Mono<ResponseEntity<Card>> registerCard(Mono<AddCardReq> addCardReq, ServerWebExchange exchange) {
    return cService.registerCard(addCardReq.cache())
    .map(c -> status(HttpStatus.CREATED).body(assembler.entityToModel(c, exchange)));
  }
}
