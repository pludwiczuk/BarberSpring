package com.spring.barber.service;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ServiceExistValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceExist {

    String message() default "Service does not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}