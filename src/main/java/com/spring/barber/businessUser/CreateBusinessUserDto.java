package com.spring.barber.businessUser;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.spring.barber.business.BusinessExist;
import com.spring.barber.user.Role;
import com.spring.barber.user.validator.HasRole;
import com.spring.barber.user.validator.UserExist;

import lombok.Getter;
import lombok.Setter;

interface ValidationStepOne {}
interface ValidationStepTwo {}
interface ValidationStepThree {}

@Getter
@Setter
@GroupSequence({CreateBusinessUserDto.class, ValidationStepOne.class, ValidationStepTwo.class, ValidationStepThree.class})
public class CreateBusinessUserDto {

  @BusinessExist(groups = ValidationStepTwo.class)
  @Positive(message = "'businessId' must be positive integer", groups = ValidationStepOne.class)
  @NotNull(message = "'businessId' cannot be null", groups = ValidationStepOne.class)
  private long businessId;

  @HasRole(role = Role.ROLE_BUSINESS_USER, groups = ValidationStepThree.class)
  @UserExist(groups = ValidationStepTwo.class)
  @Positive(message = "'userId' must be positive integer", groups = ValidationStepOne.class)
  @NotNull(message = "'userId' cannot be null", groups = ValidationStepOne.class)
  private long userId;

}
