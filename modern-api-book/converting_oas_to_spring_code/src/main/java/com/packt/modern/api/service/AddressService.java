package com.packt.modern.api.service;

import com.packt.modern.api.model.AddAddressReq;
import com.packt.modern.api.entity.AddressEntity;

import java.util.Optional;

public interface AddressService {
  Optional<AddressEntity> createAddress(AddAddressReq addAddressReq);

  void deleteAddressById(String id);

  Optional<AddressEntity> getAddressById(String id);

  Iterable<AddressEntity> getAllAddresses();
}
