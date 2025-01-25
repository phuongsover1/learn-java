package com.packt.modern.api.client;

import com.packt.modern.api.grpc.v1.ChargeServiceGrpc;
import com.packt.modern.api.grpc.v1.SourceServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class GrpcClient {
  @Value("${grpc.server.host:localhost}")
  private String host;
  @Value("${grpc.server.port:8080}")
  private int port;
  private ManagedChannel channel;
  private SourceServiceGrpc.SourceServiceBlockingStub sourceServiceStub;
  private ChargeServiceGrpc.ChargeServiceBlockingStub chargeServiceStub;

  public void start() {
    channel = ManagedChannelBuilder.forAddress(host,port)
            .usePlaintext().build();
    sourceServiceStub = SourceServiceGrpc.newBlockingStub(channel);
    chargeServiceStub = ChargeServiceGrpc.newBlockingStub(channel);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(
            1, TimeUnit.SECONDS
    );
  }

  public SourceServiceGrpc.SourceServiceBlockingStub getSourceServiceStub() {
    return sourceServiceStub;
  }

  public ChargeServiceGrpc.ChargeServiceBlockingStub getChargeServiceStub() {
    return chargeServiceStub;
  }
}
