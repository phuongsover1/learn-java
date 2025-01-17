package com.packt.modern.api.server.repository;

import com.packt.modern.api.grpc.v1.CreateSourceReq;
import com.packt.modern.api.grpc.v1.SourceId;
import com.packt.modern.api.grpc.v1.UpdateSourceReq;

public interface SourceRepository {
  CreateSourceReq.Response create(CreateSourceReq req);

  UpdateSourceReq.Response update(UpdateSourceReq req);

  SourceId.Response get(String sourceId);
}
