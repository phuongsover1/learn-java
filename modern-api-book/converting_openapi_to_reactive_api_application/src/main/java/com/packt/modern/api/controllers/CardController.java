package com.packt.modern.api.controllers;

import com.packt.modern.api.CardApi;
import com.packt.modern.api.model.AddCardReq;
import com.packt.modern.api.model.Card;
import com.packt.modern.api.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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
  public ResponseEntity<Void> deleteCardById(String id) {
    cService.deleteCardById(id);
    return accepted().build();
  }

  @Override
  public ResponseEntity<Card> getCardById(String id) {
    return cService.getCardById(id).map(assembler::toModel).map(ResponseEntity::ok).orElse(notFound().build());
  }

  @Override
  public ResponseEntity<List<Card>> getAllCards() {
    return ok(assembler.toModelList(cService.getAllCards()));
  }

  @Override
  public ResponseEntity<Card> registerCard(AddCardReq addCardReq) {
    return status(HttpStatus.CREATED).body(cService.registerCard(addCardReq).map(assembler::toModel).get());
  }
}
