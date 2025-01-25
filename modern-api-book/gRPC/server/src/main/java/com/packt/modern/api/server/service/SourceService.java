package com.packt.modern.api.server.service;

import com.packt.modern.api.grpc.v1.*;
import com.packt.modern.api.server.exception.ExceptionUtils;
import com.packt.modern.api.server.repository.SourceRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class SourceService extends SourceServiceGrpc.SourceServiceImplBase {
  private final SourceRepository repository;

  public SourceService(SourceRepository repository) {
    this.repository = repository;
  }

  @Override
  public void create(CreateSourceReq request, StreamObserver<CreateSourceReq.Response> responseObserver) {
    CreateSourceReq.Response resp = repository.create(request);
    responseObserver.onNext(resp);
    responseObserver.onCompleted();
  }

  @Override
  public void retrieve(SourceId request, StreamObserver<SourceId.Response> responseObserver) {
    try {
      SourceId.Response resp = repository.get(request.getId());
      responseObserver.onNext(resp);
      responseObserver.onCompleted();
    } catch (Exception e) {
      ExceptionUtils.observeError(responseObserver, e, SourceId.Response.getDefaultInstance());
    }
  }

  @Override
  public void update(UpdateSourceReq request, StreamObserver<UpdateSourceReq.Response> responseObserver) {
    UpdateSourceReq.Response resp = repository.update(request);
    responseObserver.onNext(resp);
    responseObserver.onCompleted();
  }

  @Override
  public void attach(AttachOrDetachReq request, StreamObserver<AttachOrDetachReq.Response> responseObserver) {
    AttachOrDetachReq.Response resp = repository.attach(request);
    responseObserver.onNext(resp);
    responseObserver.onCompleted();
  }

  @Override
  public void detach(AttachOrDetachReq request, StreamObserver<AttachOrDetachReq.Response> responseObserver) {
    AttachOrDetachReq.Response resp = repository.detach(request);
    responseObserver.onNext(resp);
    responseObserver.onCompleted();
  }
}
