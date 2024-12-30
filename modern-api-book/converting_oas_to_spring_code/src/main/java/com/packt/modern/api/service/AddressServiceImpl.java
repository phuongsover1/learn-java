package com.packt.modern.api.service;

import com.packt.modern.api.model.AddAddressReq;
import com.packt.modern.api.entity.AddressEntity;
import com.packt.modern.api.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AddressServiceImpl implements  AddressService{
  private final AddressRepository addressRepository;

  public AddressServiceImpl(AddressRepository addressRepository) {
    this.addressRepository = addressRepository;
  }

  @Override
  public Optional<AddressEntity> createAddress(AddAddressReq addAddressReq) {
    return Optional.of(addressRepository.save(toEntity(addAddressReq)));
  }

  @Override
  public void deleteAddressById(String id) {
      addressRepository.deleteById(UUID.fromString(id));
  }

  @Override
  public Optional<AddressEntity> getAddressById(String id) {
    return addressRepository.findById(UUID.fromString(id));
  }

  @Override
  public Iterable<AddressEntity> getAllAddresses() {
    return addressRepository.findAll();
  }

  private AddressEntity toEntity(AddAddressReq addAddressReq) {
    AddressEntity addressEntity = new AddressEntity();
    return addressEntity.setCity(addAddressReq.getCity())
        .setCountry(addAddressReq.getCountry())
        .setNumber(addAddressReq.getNumber())
        .setPincode(addAddressReq.getPincode())
        .setStreet(addAddressReq.getStreet())
        .setResidency(addAddressReq.getResidency());
  }
}
