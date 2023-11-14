package com.project.starcoffee.validation.phone;

import com.project.starcoffee.controller.request.member.EmailRequest;
import com.project.starcoffee.controller.request.member.PhoneRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<ValidPhone, PhoneRequest> {
    private static final String PHONE_PATTERN  = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
    }

    @Override
    public boolean isValid(PhoneRequest phone, ConstraintValidatorContext context) {
        return Pattern.matches(PHONE_PATTERN, phone.getAfterPhoneNumber());
    }

}
