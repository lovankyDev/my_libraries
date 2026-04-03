package com.washinggod.remkey.validation;


import com.washinggod.remkey.validation.validator.PasswordValidator;
import com.washinggod.remkey.validation.validator.UsernameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {UsernameValidator.class}
)
public @interface UsernameConstraint {
    String message() default "Username invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
