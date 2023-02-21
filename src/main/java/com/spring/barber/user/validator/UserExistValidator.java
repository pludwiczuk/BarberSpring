package com.spring.barber.user.validator;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.spring.barber.user.UserRepository;

public class UserExistValidator implements ConstraintValidator<UserExist, Long> {

  @Autowired
  private UserRepository repository;

  @Override
  public void initialize(UserExist constraintAnnotation) {
      ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
      return repository.existsById(id);
  }

}