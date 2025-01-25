package com.packt.modern.api;

import static org.junit.jupiter.api.Assertions.*;

import com.packt.modern.api.grpc.v1.*;
import com.packt.modern.api.server.GrpcServer;
import com.packt.modern.api.server.GrpcServerRunner;
import com.packt.modern.api.server.interceptor.ExceptionInterceptor;
import com.packt.modern.api.server.service.ChargeService;
import com.packt.modern.api.server.service.SourceService;
import io.grpc.StatusRuntimeException;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import java.io.IOException;
import org.junit.Rule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ServerAppTests {

  @Autowired private ApplicationContext context;

  /**
   * This rule manages automatic graceful shutdown for the registered servers and channels at the
   * end of test. It was kept static to perform some static operations this class methods. You may
   * refactor if required.
   */
  @Rule public static final GrpcCleanupRule grpcCleanupRule = new GrpcCleanupRule();

  private static SourceServiceGrpc.SourceServiceBlockingStub sourceBlockingStub;
  private static ChargeServiceGrpc.ChargeServiceBlockingStub chargeBlockingStub;
  private static String newlyCreatedSourceId = null;
  private static String customerId = "ab1ab2ab3";

  /**
   * To test the server, make calls with a real stub using the in-process channel, and verify
   * behaviours or state changes from the client side.
   */
  @BeforeAll
  public static void setup(
      @Autowired SourceService sourceService,
      @Autowired ChargeService chargeService,
      @Autowired ExceptionInterceptor interceptor)
      throws IOException {
    // Generate a unique in-process server name.
    String serverName = InProcessServerBuilder.generateName();

    // Create a server, and service, start, and register for automatic graceful shutdown.
    grpcCleanupRule.register(
        InProcessServerBuilder.forName(serverName)
            .directExecutor()
            .addService(sourceService)
            .addService(chargeService)
            .build()
            .start());

    sourceBlockingStub =
        SourceServiceGrpc.newBlockingStub(
            // Create a client channel and register for automatic graceful shutdown.
            grpcCleanupRule.register(
                InProcessChannelBuilder.forName(serverName).directExecutor().build()));

    chargeBlockingStub =
        ChargeServiceGrpc.newBlockingStub(
            // Create a client channel and register for automatic graceful shutdown.
            grpcCleanupRule.register(
                InProcessChannelBuilder.forName(serverName).directExecutor().build()));
  }

  @Test
  @Order(1)
  void beanGrpcServerRunnerTest() {
    assertNotNull(context.getBean(GrpcServer.class));
    assertThrows(
        NoSuchBeanDefinitionException.class,
        () -> context.getBean(GrpcServerRunner.class),
        "GrpcServerRunner should not be loaded during test");
  }

  @Test
  @Order(2)
  @DisplayName("Creates source object using create RPC call")
  public void SourceService_Create() {
    CreateSourceReq.Response response =
        sourceBlockingStub.create(
            CreateSourceReq.newBuilder().setAmount(100).setCurrency("USD").build());
    assertNotNull(response);
    assertNotNull(response.getSource());
    newlyCreatedSourceId = response.getSource().getId();
    assertEquals(100, response.getSource().getAmount());
    assertEquals("USD", response.getSource().getCurrency());
  }

  @Test
  @Order(3)
  @DisplayName("Throws exception when invalid source id is passed to retrieve RPC call")
  public void SourceService_RetrieveInvalidSourceId() {
    Throwable throwable =
        assertThrows(
            StatusRuntimeException.class,
            () -> sourceBlockingStub.retrieve(SourceId.newBuilder().setId("").build()));
    assertEquals("INVALID_ARGUMENT: Invalid Source ID is passed.", throwable.getMessage());
  }

  @Test
  @Order(3)
  @DisplayName("Creates Charge using create RPC call")
  public void ChargeService_CreateCharge() {
    CreateChargeReq.Response resp =
        chargeBlockingStub.create(
            CreateChargeReq.newBuilder()
                .setSourceId(newlyCreatedSourceId)
                .setAmount(100)
                .setCurrency("USD")
                .setCustomerId(customerId)
                .build());
    assertNotNull(resp);
    assertNotNull(resp.getCharge());
    assertEquals(100, resp.getCharge().getAmount());
    assertEquals("USD", resp.getCharge().getCurrency());
    assertEquals(customerId, resp.getCharge().getCustomerId());
    assertEquals(newlyCreatedSourceId, resp.getCharge().getSourceId());
  }

  @Test
  @Order(4)
  @DisplayName("Retrieves source obj created by createRPC call")
  public void SourceService_Retrieve() {
    SourceId.Response response =
        sourceBlockingStub.retrieve(SourceId.newBuilder().setId(newlyCreatedSourceId).build());
    assertNotNull(response);
    assertNotNull(response.getSource());
    assertEquals(100, response.getSource().getAmount());
    assertEquals("USD", response.getSource().getCurrency());
  }

//  @Test
//  @DisplayName("Retrieves charges by customerId")
//  public void ChargeService_RetrieveAllByCustomer() {}
}
