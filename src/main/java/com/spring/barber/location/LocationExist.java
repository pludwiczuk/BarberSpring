package com.spring.barber.location;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LocationExistValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface LocationExist {

    String message() default "Location does not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}