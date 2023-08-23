package com.project.starcoffee.validation.password;

import com.project.starcoffee.controller.request.PasswordRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, PasswordRequest> {
    private static final String PASSWORD_PATTERN
            = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$";

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(PasswordRequest password, ConstraintValidatorContext context) {
        return Pattern.matches(PASSWORD_PATTERN, password.getAfterPassword());
    }

}
