package com.basecamp.rest.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Constraint(validatedBy = CityNameValidator.class)
public @interface CityName {
    String message() default "Wrong city name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
