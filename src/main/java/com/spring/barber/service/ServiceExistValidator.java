package com.spring.barber.service;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ServiceExistValidator implements ConstraintValidator<ServiceExist, Long> {

  @Autowired
  private ServiceRepository repository;

  @Override
  public void initialize(ServiceExist constraintAnnotation) {
      ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
      return repository.existsById(id);
  }

}