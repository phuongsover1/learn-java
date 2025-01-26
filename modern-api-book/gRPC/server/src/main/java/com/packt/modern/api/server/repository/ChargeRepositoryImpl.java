package com.packt.modern.api.server.repository;

import com.packt.modern.api.grpc.v1.ChargeId;
import com.packt.modern.api.grpc.v1.CreateChargeReq;
import com.packt.modern.api.grpc.v1.CustomerId;
import com.packt.modern.api.grpc.v1.UpdateChargeReq;
import org.springframework.stereotype.Repository;

@Repository
public class ChargeRepositoryImpl implements ChargeRepository {

  private final DbStore dbStore;

  public ChargeRepositoryImpl(DbStore dbStore) {
    this.dbStore = dbStore;
  }

  @Override
  public CreateChargeReq.Response create(CreateChargeReq req) {
    return dbStore.createCharge(req);
  }

  @Override
  public UpdateChargeReq.Response update(UpdateChargeReq req) {
    return dbStore.updateCharge(req);
  }

  @Override
  public ChargeId.Response retrieve(String chargeId) {
    return dbStore.retrieveCharge(chargeId);
  }

  @Override
  public CustomerId.Response retrieveAllCharges(String customerId) {
    return dbStore.retrieveAllCharges(customerId);
  }
}
