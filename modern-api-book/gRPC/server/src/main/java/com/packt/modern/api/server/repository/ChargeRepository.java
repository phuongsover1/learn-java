package com.packt.modern.api.server.repository;

import com.packt.modern.api.grpc.v1.ChargeId;
import com.packt.modern.api.grpc.v1.CreateChargeReq;
import com.packt.modern.api.grpc.v1.CustomerId;
import com.packt.modern.api.grpc.v1.UpdateChargeReq;

public interface ChargeRepository {
  CreateChargeReq.Response create(CreateChargeReq req);

  UpdateChargeReq.Response update(UpdateChargeReq req);

  ChargeId.Response retrieve(String chargeId);

  CustomerId.Response retrieveAllCharges(String customerId);
}
