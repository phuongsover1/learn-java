package com.packt.modern.api.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.security.core.GrantedAuthority;

public enum RoleEnum implements GrantedAuthority {
  USER(Const.USER),
  ADMIN(Const.ADMIN),
  CSR(Const.CSR);

  private String authority;

  RoleEnum(String authority) {
    this.authority = authority;
  }

  @JsonCreator
  public static RoleEnum fromAuthority(String authority) {
    for (RoleEnum role : RoleEnum.values()) {
      if (role.authority.equals(authority)) {
        return role;
      }
    }
    throw new IllegalArgumentException("Invalid authority: " + authority);
  }

  @Override
  public String toString() {
    return String.valueOf(authority);
  }

  @Override
  @JsonValue
  public String getAuthority() {
    return "";
  }

  public class Const {
    public static final String USER = "ROLE_USER";
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String CSR = "ROLE_CSR";
  }
}