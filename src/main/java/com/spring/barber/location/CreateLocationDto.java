package com.spring.barber.location;

import com.spring.barber.address.AddressDto;
import com.spring.barber.business.BusinessExist;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class CreateLocationDto {

    @NotEmpty(message = "'name' must not be empty")
    private String name;

    @BusinessExist
    @Positive(message = "'businessId' must be positive integer")
    @NotNull(message = "'businessId' cannot be null")
    private long businessId;

    @Valid
    @NotNull(message = "'address' must be present")
    private AddressDto address;

}
