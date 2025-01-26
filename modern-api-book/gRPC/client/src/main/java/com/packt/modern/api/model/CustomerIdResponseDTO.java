package com.packt.modern.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public class CustomerIdResponseDTO {
  private List<Charge> charge;

  public List<Charge> getCharge() {
    return charge;
  }

  public void setCharge(List<Charge> charge) {
    this.charge = charge;
  }
}
