package com.packt.modern.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.packt.modern.api.client.GrpcClient;
import com.packt.modern.api.grpc.v1.CustomerId;
import com.packt.modern.api.model.Charge;
import com.packt.modern.api.model.CustomerIdResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.List;

@RestController
public class ChargeController {
  private final GrpcClient client;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public ChargeController(GrpcClient client) {
    this.client = client;
  }

  /**
   *  TODO:  + Trả về error lỗi dễ nhìn
   *         + Tạo unit test cho create, retrieve source va charge
    */
  @GetMapping("/charges")
  public ResponseEntity<CustomerIdResponseDTO> getSources(@RequestParam String customerId) throws InvalidProtocolBufferException, JsonProcessingException {
    var req = CustomerId.newBuilder().setId(customerId).build();
    CustomerId.Response resp = client.getChargeServiceStub().retrieveAll(req);
    var printer = JsonFormat.printer().includingDefaultValueFields();
    CustomerIdResponseDTO charges = objectMapper.readValue(printer.print(resp), CustomerIdResponseDTO.class);
    return new ResponseEntity<>(charges, HttpStatus.OK);
  }
}
