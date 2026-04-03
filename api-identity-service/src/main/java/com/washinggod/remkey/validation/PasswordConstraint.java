package com.washinggod.remkey.validation;


import com.washinggod.remkey.validation.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {PasswordValidator.class}
)
public @interface PasswordConstraint {
    String message() default "Password invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
