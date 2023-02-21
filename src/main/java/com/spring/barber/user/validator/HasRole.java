package com.spring.barber.user.validator;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.spring.barber.user.Role;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = HasRoleValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface HasRole {

  String message() default "User does not have required role";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};

  Role role();

}