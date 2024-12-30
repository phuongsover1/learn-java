package com.packt.modern.api.service;

import com.packt.modern.api.entity.AddressEntity;
import com.packt.modern.api.model.AddAddressReq;
import com.packt.modern.api.repository.AddressRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {
  private final static String id = "a1b9b31d-e73c-4112-af7c-b68530f38222";
  private final static String nonExistId = "a1b9b31d-e73c-4112-af7c-b68530f38220";
  private static AddressEntity entity;
  private static AddAddressReq addressReq;
  @Mock
  private AddressRepository repository;
  @InjectMocks
  private AddressServiceImpl service;

  @BeforeAll
  public static void setup() {
    entity = new AddressEntity()
            .setId(UUID.fromString(id))
            .setNumber("111").setResidency("Residency").setStreet("Street")
            .setCity("City").setCountry("Country").setPincode("12345");
    addressReq = new AddAddressReq().id(entity.getId().toString()).number(entity.getNumber())
            .residency(entity.getResidency()).street(entity.getStreet()).city(entity.getCity())
            .country(entity.getCountry()).pincode(entity.getPincode());
  }

  @Test
  @DisplayName("returns an AddressEntity when private method toEntity() is called with AddAddressReq model")
  public void convertModelToEntity() {
    // given
    AddressServiceImpl srvc = new AddressServiceImpl(repository);
    // when
     AddressEntity e =  ReflectionTestUtils.invokeMethod(srvc, "toEntity", addressReq);
     // then
    then(e).as("Check address entity is returned & not null")
            .isNotNull();
    then(e.getNumber()).as("Check house/flat number is set").isEqualTo(entity.getNumber());
    then(e.getResidency()).as("Check residency is set").isEqualTo(entity.getResidency());
    then(e.getStreet()).as("Check street is set").isEqualTo(entity.getStreet());
    then(e.getCity()).as("Check city is set").isEqualTo(entity.getCity());
    then(e.getCountry()).as("Check country is set").isEqualTo(entity.getCountry());
    then(e.getPincode()).as("Check pincode is set").isEqualTo(entity.getPincode());
  }

  @Test
  @DisplayName("delete address by given exising id")
  public void deleteAddressesByExistId() {
    // given
    given(repository.findById(UUID.fromString(id))).willReturn(Optional.of(entity));
    willDoNothing().given(repository).deleteById(UUID.fromString(id));

    // when
    service.deleteAddressById(id);

    // then
    verify(repository, times(1)).findById(UUID.fromString(id));
    verify(repository, times(1)).deleteById(UUID.fromString(id));
  }
}

