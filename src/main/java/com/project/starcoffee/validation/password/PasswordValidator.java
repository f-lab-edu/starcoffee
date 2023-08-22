package com.project.starcoffee.validation.password;

import com.project.starcoffee.controller.request.PasswordRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, PasswordRequest> {

    private static final int MIN_SIZE = 8;
    private static final int MAX_SIZE = 20;
    private static final String PASSWORD_PATTERN
            = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{"+MIN_SIZE+ "," +MAX_SIZE+ "}$";

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(PasswordRequest password, ConstraintValidatorContext context) {
        boolean isValidPassword = Pattern.matches(PASSWORD_PATTERN, password.getAfterPassword());

        if (!isValidPassword) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    MessageFormat.format("{0}자 이상의 {1}자 이하의 숫자, 영문, 특수문자를 포함한 비밀번호를 입력해주세요",
                            MIN_SIZE, MAX_SIZE)).addConstraintViolation();
        }

        return isValidPassword;

    }
}
