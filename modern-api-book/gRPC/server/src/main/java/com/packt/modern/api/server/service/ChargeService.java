package com.packt.modern.api.server.service;

import com.packt.modern.api.grpc.v1.*;
import com.packt.modern.api.server.exception.ExceptionUtils;
import com.packt.modern.api.server.repository.ChargeRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class ChargeService extends ChargeServiceGrpc.ChargeServiceImplBase {
  private final ChargeRepository repository;

  public ChargeService(ChargeRepository repository) {
    this.repository = repository;
  }

  @Override
  public void create(CreateChargeReq request, StreamObserver<CreateChargeReq.Response> responseObserver) {
    CreateChargeReq.Response resp = repository.create(request);
    responseObserver.onNext(resp);
    responseObserver.onCompleted();
  }

  @Override
  public void retrieve(ChargeId request, StreamObserver<ChargeId.Response> responseObserver) {
    ChargeId.Response resp = repository.retrieve(request.getId());
    responseObserver.onNext(resp);
    responseObserver.onCompleted();
  }

  @Override
  public void update(UpdateChargeReq request, StreamObserver<UpdateChargeReq.Response> responseObserver) {
    UpdateChargeReq.Response resp = repository.update(request);
    responseObserver.onNext(resp);
    responseObserver.onCompleted();
  }

  @Override
  public void retrieveAll(CustomerId request, StreamObserver<CustomerId.Response> responseObserver) {
    try {
      CustomerId.Response resp = repository.retrieveAllCharges(request.getId());
      responseObserver.onNext(resp);
      responseObserver.onCompleted();
    } catch (Exception e) {
      ExceptionUtils.observeError(responseObserver, e, CustomerId.Response.getDefaultInstance());
    }
  }
}
