package com.spring.barber.business;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.spring.barber.address.AddressDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBusinessDto {

  @NotEmpty(message = "'name' must not be empty")
  private String name;

  @Valid
  @NotNull(message = "'address' must be present")
  private AddressDto address;
  
}
