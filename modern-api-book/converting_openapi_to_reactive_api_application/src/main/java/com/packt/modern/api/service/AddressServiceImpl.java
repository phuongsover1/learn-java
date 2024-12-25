package com.packt.modern.api.service;

import com.packt.modern.api.entity.AddressEntity;
import com.packt.modern.api.entity.UserEntity;
import com.packt.modern.api.exceptions.CustomerNotFoundException;
import com.packt.modern.api.exceptions.ErrorCode;
import com.packt.modern.api.model.AddAddressReq;
import com.packt.modern.api.model.Address;
import com.packt.modern.api.model.User;
import com.packt.modern.api.repository.AddressRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class AddressServiceImpl implements AddressService {
  private final AddressRepository addressRepository;

  private final UserService uService;

  public AddressServiceImpl(AddressRepository addressRepository, UserService uService) {
    this.addressRepository = addressRepository;
    this.uService = uService;
  }

  @Override
  public Mono<AddressEntity> createAddress(Mono<AddAddressReq> addAddressReq) {
    return addAddressReq.flatMap(addReq -> {
      if (Objects.isNull(addReq.getUserId())) {
        return addressRepository.save(toEntity(addReq));
      }
      return uService.getCustomerById(addReq.getUserId())
          .switchIfEmpty(Mono.error(new CustomerNotFoundException(
              "Requested address with user's id " + addReq.getUserId() + " not exist!!!")))
          .flatMap(u -> addressRepository.save(toEntity(addReq)))
          .flatMap(savedAdd -> {
          return addressRepository.saveMapping(UUID.fromString(addReq.getUserId()), savedAdd.getId())
            .thenReturn(savedAdd);
          });
    });
  }

  // TODO: làm thêm là trước khi xoá address thì xoá trong bảng user_address trước 
  @Override
  public Mono<Void> deleteAddressById(String id) {
    return addressRepository.deleteById(UUID.fromString(id));
  }

  @Override
  public Mono<AddressEntity> getAddressById(String id) {
    return addressRepository.findById(UUID.fromString(id));
  }

  @Override
  public Flux<AddressEntity> getAllAddresses() {
    return addressRepository.findAll();
  }

  @Override
  public Address toModel(AddressEntity entity) {
    Address address = new Address();
    BeanUtils.copyProperties(entity, address);
    return address;
  }

  @Override
  public AddressEntity toEntity(AddAddressReq addAddressReq) {
    AddressEntity addressEntity = new AddressEntity();
    return addressEntity.setCity(addAddressReq.getCity())
        .setCountry(addAddressReq.getCountry())
        .setNumber(addAddressReq.getNumber())
        .setPincode(addAddressReq.getPincode())
        .setStreet(addAddressReq.getStreet());

  }

  public Mono<List<User>> getUpdatedList(List<UserEntity> users, UserEntity savedUser) {
    users.add(savedUser);
    return Mono.just(users.stream().map(uService::toModel).toList());
  }
}
