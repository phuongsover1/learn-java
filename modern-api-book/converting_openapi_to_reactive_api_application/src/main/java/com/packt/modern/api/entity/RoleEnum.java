package com.packt.modern.api.entity;

import org.springframework.security.core.GrantedAuthority;

public enum RoleEnum implements GrantedAuthority {
  USER(Const.USER),
  ADMIN(Const.ADMIN);

  private String authority;

  RoleEnum(String authority) {
    this.authority = authority;
  }

  @Override
  public String getAuthority() {
    return authority;
  }

  @Override
  public String toString() {
    return String.valueOf(authority);
  }


  public class Const {
    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";
  }
}
