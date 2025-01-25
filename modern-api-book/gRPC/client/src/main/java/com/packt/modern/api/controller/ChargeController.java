package com.packt.modern.api.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.packt.modern.api.client.GrpcClient;
import com.packt.modern.api.grpc.v1.CustomerId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChargeController {
  private final GrpcClient client;

  public ChargeController(GrpcClient client) {
    this.client = client;
  }

  @GetMapping("/charges")
  public String getSources(@RequestParam(defaultValue = "ab1ab2ab3ab4ab5") String customerId) throws InvalidProtocolBufferException {
    var req = CustomerId.newBuilder().setId(customerId).build();
    CustomerId.Response resp = client.getChargeServiceStub().retrieveAll(req);
    var printer = JsonFormat.printer().includingDefaultValueFields();
    return printer.print(resp);
  }
}
