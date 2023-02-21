package com.spring.barber.business;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BusinessExistValidator implements ConstraintValidator<BusinessExist, Long> {

    @Autowired
    private BusinessRepository repository;

    @Override
    public void initialize(BusinessExist constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
        return repository.existsById(id);
    }
}
