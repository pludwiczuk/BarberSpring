package com.spring.barber.business;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessResponse {
 
  private Long id;
  private String name;

  public BusinessResponse(Business business) {
    this.id = business.getId();
    this.name = business.getName();
  }

}
