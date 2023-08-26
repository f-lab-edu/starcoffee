package com.project.starcoffee.validation.email;

import com.project.starcoffee.controller.request.member.EmailRequest;
import com.project.starcoffee.controller.request.member.PasswordRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, EmailRequest> {
    private static final String PASSWORD_PATTERN  = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(EmailRequest email, ConstraintValidatorContext context) {
        return Pattern.matches(PASSWORD_PATTERN, email.getAfterEmail());
    }

}
