package com.spring.barber.business;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BusinessExistValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface BusinessExist {

    String message() default "Business does not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
