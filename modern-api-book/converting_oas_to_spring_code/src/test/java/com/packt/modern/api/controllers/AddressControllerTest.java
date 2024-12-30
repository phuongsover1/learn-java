package com.packt.modern.api.controllers;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.packt.modern.api.config.AppConfig;
import com.packt.modern.api.entity.AddressEntity;
import com.packt.modern.api.exceptions.RestApiErrorHandler;
import com.packt.modern.api.hateoas.AddressRepresentationModelAssembler;
import com.packt.modern.api.model.AddAddressReq;
import com.packt.modern.api.model.Address;
import com.packt.modern.api.service.AddressService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class AddressControllerTest {

  private static final String URI = "/api/v1/addresses";
  private static final String id = "a1b9b31d-e73c-4112-af7c-b68530f38222";

  private static AddressService service = mock(AddressService.class);
  private static MessageSource messageSource = mock(MessageSource.class);
  private static AddressController controller;

  private static MockMvc mockMvc;

  private static AddressEntity entity;
  private static Address model;
  private static AddAddressReq req;

  private JacksonTester<Address> addressJacksonTester;
  private JacksonTester<AddAddressReq> addAddressReqJacksonTester;

  @BeforeAll
  public static void setup() {
    controller = new AddressController(service, new AddressRepresentationModelAssembler());
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(new RestApiErrorHandler(messageSource))
            .alwaysDo(print())
            .build();

    entity =
        new AddressEntity()
            .setId(UUID.fromString(id))
            .setNumber("111")
            .setResidency("Residency")
            .setStreet("Street")
            .setCity("City")
            .setCountry("Country")
            .setPincode("12345");
    req =
        new AddAddressReq()
            .id(entity.getId().toString())
            .number(entity.getNumber())
            .residency(entity.getResidency())
            .street(entity.getStreet())
            .pincode(entity.getPincode());
    model =
        new Address()
            .id(entity.getId().toString())
            .number(entity.getNumber())
            .residency(entity.getResidency())
            .street(entity.getStreet())
            .city(entity.getCity())
            .country(entity.getCountry())
            .pincode(entity.getPincode());
  }

  @BeforeEach
  public void configureJsonTester() {
    JacksonTester.initFields(this, new AppConfig().objectMapper());
  }

  @Test
  @DisplayName("returns address by given existing ID")
  void getAddressesByIdWhenExists() throws Exception {
    // given
    given(service.getAddressById(id)).willReturn(Optional.of(entity));

    // when
    ResultActions result =
        mockMvc.perform(
            get("/api/v1/addresses/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

    // then
    result.andExpect(status().isOk());
    verifyJson(result);
  }

  private void verifyJson(ResultActions result) throws Exception {
    final String BASE_PATH = "http://localhost";
    result
        .andExpect(jsonPath("id", is(entity.getId().toString())))
        .andExpect(jsonPath("number", is(entity.getNumber())))
        .andExpect(jsonPath("residency", is(entity.getResidency())))
        .andExpect(jsonPath("street", is(entity.getStreet())))
        .andExpect(jsonPath("city", is(entity.getCity())))
        .andExpect(jsonPath("country", is(entity.getCountry())))
        .andExpect(jsonPath("pincode", is(entity.getPincode())))
        .andExpect(jsonPath("links[0].rel", is("self")))
        .andExpect(jsonPath("links[0].href", is(BASE_PATH + URI + "/" + entity.getId())));
  }
}
