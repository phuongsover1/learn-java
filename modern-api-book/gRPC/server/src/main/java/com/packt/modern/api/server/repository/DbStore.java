package com.packt.modern.api.server.repository;

import com.google.protobuf.Any;
import com.google.rpc.Code;
import com.packt.modern.api.grpc.v1.*;
import io.grpc.protobuf.StatusProto;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

@Component
public class DbStore {
  private static final Map<String, Source> sourceEntities = new ConcurrentHashMap<>();
  private static final Map<String, Charge> chargeEntities = new ConcurrentHashMap<>();

  public DbStore() {
    Source source =
        Source.newBuilder()
            .setId(RandomHolder.randomKey())
            .setType("card")
            .setAmount(100)
            .setOwner(createOwner())
            .setReceiver(createReceiver())
            .setCurrency("USD")
            .setStatementDescriptor("Statement")
            .setFlow(Flow.RECEIVER)
            .setUsage(Usage.REUSABLE)
            .setCreated(Instant.now().getEpochSecond())
            .build();
    sourceEntities.put(source.getId(), source);

    Charge charge =
        Charge.newBuilder()
            .setId(RandomHolder.randomKey())
            .setAmount(1000)
            .setCurrency("USD")
            .setCustomerId("ab1ab2ab3ab4ab5")
            .setDescription("ChargeDescription")
            .setReceiptEmail("receipt@email.com")
            .setStatementDescriptor("Statement Descriptor")
            .setSourceId(source.getId())
            .setCreated(Instant.now().getEpochSecond())
            .build();

    chargeEntities.put(charge.getId(), charge);
  }

  public CreateSourceReq.Response createSource(CreateSourceReq req) {
    Source source =
        Source.newBuilder()
            .setId(RandomHolder.randomKey())
            .setType(req.getType())
            .setAmount(req.getAmount())
            .setOwner(createOwner())
            .setReceiver(createReceiver())
            .setCurrency(req.getCurrency())
            .setStatementDescriptor(req.getStatementDescriptor())
            .setFlow(req.getFlow())
            .setCreated(Instant.now().getEpochSecond())
            .setUsage(req.getUsage())
            .build();
    sourceEntities.put(source.getId(), source);
    return CreateSourceReq.Response.newBuilder().setSource(source).build();
  }

  public UpdateSourceReq.Response updateSource(UpdateSourceReq req) {
    Source source = sourceEntities.get(req.getSourceId());
    if (Objects.isNull(source)) {
      com.google.rpc.Status status =
          com.google.rpc.Status.newBuilder()
              .setCode(Code.INVALID_ARGUMENT.getNumber())
              .setMessage("Requested source is not available")
              .build();
      throw io.grpc.Status.INVALID_ARGUMENT
          .withDescription(status.getMessage())
          .asRuntimeException();
    }
    source = source.toBuilder().setAmount(req.getAmount()).setOwner(req.getOwner()).build();
    sourceEntities.put(source.getId(), source);
    return UpdateSourceReq.Response.newBuilder().setSource(source).build();
  }

  public SourceId.Response retrieveSource(String sourceId) {
    if (Strings.isBlank(sourceId)) {
      com.google.rpc.Status status = com.google.rpc.Status.newBuilder()
              .setCode(Code.INVALID_ARGUMENT.getNumber())
              .setMessage("Invalid Source ID is passed.")
              .build();
      throw StatusProto.toStatusRuntimeException(status);
    }
    Source source = sourceEntities.get(sourceId);
    if (Objects.isNull(source)) {
      throw io.grpc.Status.INVALID_ARGUMENT
          .withDescription("Requested source is not available")
          .asRuntimeException();
    }
    return SourceId.Response.newBuilder().setSource(source).build();
  }

  public AttachOrDetachReq.Response attach(AttachOrDetachReq req) {
    Source source = sourceEntities.get(req.getSourceId());
    if (Objects.isNull(source)) {
          com.google.rpc.Status status = com.google.rpc.Status.newBuilder()
                  .setCode(Code.INVALID_ARGUMENT.getNumber())
                  .setMessage("Requested source is not available")
                  .addDetails(Any.pack(SourceId.Response.getDefaultInstance()))
                  .build();
          throw StatusProto.toStatusRuntimeException(status);
    }
    Source.Status status = source.getStatus();
    if (status.equals(Source.Status.CHARGEABLE) || status.equals(Source.Status.PENDING)) {
      int amt = source.getAmount();
      source = source.toBuilder().setAmount(0).setStatus(Source.Status.CHARGEABLE).build();
      sourceEntities.put(source.getId(), source);
    }
    return AttachOrDetachReq.Response.newBuilder().setSource(source).build();
  }

  public AttachOrDetachReq.Response detach(AttachOrDetachReq req) {
    Source source = sourceEntities.get(req.getSourceId());
    if (Objects.isNull(source)) {
      throw io.grpc.Status.INVALID_ARGUMENT
          .withDescription("Requested source is not available")
          .asRuntimeException();
    }
    int amt = source.getAmount();
    source = source.toBuilder().setAmount(0).setStatus(Source.Status.CONSUMNED).build();
    sourceEntities.put(source.getId(), source);
    return AttachOrDetachReq.Response.newBuilder().setSource(source).build();
  }

  public Owner createOwner() {
    Address address =
        Address.newBuilder()
            .setNumber("F-31")
            .setResidency("Residency")
            .setStreet("Street")
            .setCity("City")
            .setState("State")
            .setCountry("Country")
            .setPostalCode("12345")
            .build();
    return Owner.newBuilder()
        .setName("Scott")
        .setAddress(address)
        .setEmail("scott@packt.modern.api")
        .setPhone("123456789")
        .build();
  }

  public Receiver createReceiver() {
    return Receiver.newBuilder()
        .setAddress("Address")
        .setAmountReceived(0)
        .setAmountCharged(0)
        .setAmountReturned(0)
        .setRefundAttributesMethod(Receiver.RefundAttributesMethod.EMAIL)
        .setRefundAttributesStatus(Receiver.RefundAttributeStatus.MISSING)
        .build();
  }

  // CHARGE
  public CreateChargeReq.Response createCharge(CreateChargeReq req) {
    Charge charge =
        Charge.newBuilder()
            .setId(RandomHolder.randomKey())
            .setAmount(req.getAmount())
            .setCurrency(req.getCurrency())
            .setCustomerId(req.getCustomerId())
            .setDescription(req.getDescription())
            .setReceiptEmail(req.getReceiptEmail())
            .setStatementDescriptor(req.getStatementDescriptor())
            .setSourceId(req.getSourceId())
            .setCreated(Instant.now().getEpochSecond())
            .build();
    chargeEntities.put(charge.getId(), charge);
    return CreateChargeReq.Response.newBuilder().setCharge(charge).build();
  }

  public UpdateChargeReq.Response updateCharge(UpdateChargeReq req) {
    Charge charge = chargeEntities.get(req.getChargeId());
    if (Objects.isNull(charge)) {
      throw io.grpc.Status.INVALID_ARGUMENT.withDescription("Requested charge is not available").asRuntimeException();
    }
    Charge.Builder builder = charge.toBuilder();
    boolean chargeUpdated = false;
    String customerId = req.getCustomerId();
    if ( Strings.isNotBlank(customerId)) {
      builder.setCustomerId(customerId);
      chargeUpdated = true;
    }
    String email = req.getReceiptEmail();
    if (Strings.isNotBlank(email)) {
      builder.setReceiptEmail(email);
      chargeUpdated = true;
    }
    String desc = req.getDescription();
    if (Strings.isNotBlank(desc)) {
      builder.setDescription(desc);
      chargeUpdated = true;
    }
    if (chargeUpdated) {
      charge = builder.build();
      chargeEntities.put(charge.getId(), charge);
    }
    return UpdateChargeReq.Response.newBuilder().setCharge(charge).build();
  }

  public ChargeId.Response retrieveCharge(String chargeId) {
    Charge charge = chargeEntities.get(chargeId);
    if (Objects.isNull(charge)) {
      throw io.grpc.Status.INVALID_ARGUMENT.withDescription("Requested charge is not available").asRuntimeException();
    }
    return ChargeId.Response.newBuilder().setCharge(charge).build();
  }

  public CustomerId.Response retrieveAllCharges(String customerId) {
    if (Strings.isBlank(customerId)) {
      com.google.rpc.Status status = com.google.rpc.Status.newBuilder()
              .setCode(Code.INVALID_ARGUMENT.getNumber())
              .setMessage("Invalid customerId is passed.")
              .build();
      throw StatusProto.toStatusRuntimeException(status);
    }
    CustomerId.Response.Builder builder = CustomerId.Response.newBuilder();
    chargeEntities.values().stream()
            .filter(charge -> charge.getCustomerId().equals(customerId))
            .forEach(builder::addCharge);
    return builder.build();
  }

  private static class RandomHolder {
    private static final Random RANDOM = new SecureRandom();

    public static String randomKey() {
      return randomKey(32);
    }

    public static String randomKey(int length) {
      return String.format(
              "%" + length + "s", new BigInteger(length * 5 /*base 32,2^5*/, RANDOM).toString(32))
          .replace('\u0020', '0');
    }
  }
}
