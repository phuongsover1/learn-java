package com.packt.modern.api.controllers;

import com.packt.modern.api.AddressApi;
import com.packt.modern.api.entity.RoleEnum.Const;
import com.packt.modern.api.hateoas.AddressRepresentationModelAssembler;
import com.packt.modern.api.model.AddAddressReq;
import com.packt.modern.api.model.Address;
import com.packt.modern.api.service.AddressService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
public class AddressController implements AddressApi {

  private final AddressService addressService;
  private final AddressRepresentationModelAssembler assembler;

  public AddressController(AddressService addressService, AddressRepresentationModelAssembler assembler) {
    this.addressService = addressService;
    this.assembler = assembler;
  }

  public ResponseEntity<Address> createAddress(@Valid AddAddressReq addAddressReq) {
    return status(HttpStatus.CREATED).body(addressService.createAddress(addAddressReq).map(assembler::toModel).get());
  }

  @Override
  public ResponseEntity<Address> getAddressesById(String id) {
    return addressService.getAddressById(id).map(assembler::toModel)
        .map(ResponseEntity::ok).orElse(notFound().build());
  }

  @Override
  public ResponseEntity<List<Address>> getAllAddresses() {
      return ok(assembler.toModelList(addressService.getAllAddresses()));
  }

  @PreAuthorize("hasRole('" + Const.ADMIN +"')")
  @Override
  public ResponseEntity<Void> deleteAddressesById(String id) {
    addressService.deleteAddressById(id);
    return accepted().build();
  }
}
