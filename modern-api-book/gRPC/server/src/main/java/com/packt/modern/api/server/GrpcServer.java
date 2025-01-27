package com.packt.modern.api.server;

import com.packt.modern.api.server.interceptor.ExceptionInterceptor;
import com.packt.modern.api.server.service.ChargeService;
import com.packt.modern.api.server.service.SourceService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.micrometer.core.instrument.binder.grpc.ObservationGrpcServerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GrpcServer {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${grpc.port}")
  private int port;
  private Server server;
  private final ChargeService chargeService;
  private final SourceService sourceService;
  private final ExceptionInterceptor exceptionInterceptor;

  private final ObservationGrpcServerInterceptor oInterceptor;

  public GrpcServer(ChargeService chargeService, SourceService sourceService, ExceptionInterceptor exceptionInterceptor, ObservationGrpcServerInterceptor oInterceptor) {
    this.chargeService = chargeService;
    this.sourceService = sourceService;
    this.exceptionInterceptor = exceptionInterceptor;
    this.oInterceptor = oInterceptor;
  }

  public void start() throws IOException {
    logger.info("Starting server on port {}", port);
    server = ServerBuilder.forPort(port)
            .addService(sourceService)
            .addService(chargeService)
            .intercept(exceptionInterceptor)
            .intercept(oInterceptor)
            .build().start();
    logger.info("gRPC server started and listening on port: {}", port);
    logger.info("Following server are available: ");
    server.getServices().stream()
            .forEach(s -> logger.info("Service Name: {}", s.getServiceDescriptor().getName()));
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      logger.info("Shutting down gRPC server");
      GrpcServer.this.stop();
      logger.info("gRPC server shut down successfully!!!");
    }));
  }

  public void stop() {
    if (server != null) {
      server.shutdown();
    }
  }

  public void block() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }
}
