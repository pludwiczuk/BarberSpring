package com.spring.barber.address;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {

  @NotEmpty(message = "'line1' must not be empty")
  private String line1;

  private String line2; // can be empty

  @NotEmpty(message = "'postCode' must not be empty")
  private String postCode;

  @NotEmpty(message = "'city' must not be empty")
  private String city;

  @NotEmpty(message = "'country' must not be empty")
  private String country;

}
