package com.spring.barber.location;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LocationExistValidator implements ConstraintValidator<LocationExist, Long> {

    @Autowired
    private LocationRepository repository;

    @Override
    public void initialize(LocationExist constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
        return repository.existsById(id);
    }

}