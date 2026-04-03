package com.washinggod.remkey.validation.validator;


import com.washinggod.remkey.configuration.properties.RegexPattern;
import com.washinggod.remkey.validation.PasswordConstraint;
import com.washinggod.remkey.validation.UsernameConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class UsernameValidator implements ConstraintValidator<UsernameConstraint, String> {


    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if(Objects.isNull(s)) {
            return true;
        }

        return RegexPattern.USERNAME_REGEX.matcher(s).matches();
    }
}
