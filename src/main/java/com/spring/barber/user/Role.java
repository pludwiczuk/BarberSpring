package com.spring.barber.user;

public enum Role {

  ROLE_ADMIN("ROLE_ADMIN"),
  ROLE_BUSINESS_USER("ROLE_BUSINESS_USER"),
  ROLE_USER("ROLE_USER");

  private String value;

  private Role(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value;
  }

}
