package com.project.starcoffee.dto;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Slf4j
public class MemberDTOValidationTest {

    private MemberDTO member;

    @BeforeEach
    void init() {
        member = MemberDTO.builder()
                .id(UUID.fromString("a999d6e0-4d8b-11ee-ac76-fd3617d1d2aa"))
                .name("김")
                .tel("010-12-1234")
                .email("testnaver.com")
                .gender("M")
                .nickName("nickName")
                .loginId("testId")
                .password("1234")
                .birth(ZonedDateTime.from(LocalDateTime.now()))
                .build();
    }

    //@Test
    void memberValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<MemberDTO>> validations = validator.validate(member);

        for (ConstraintViolation<MemberDTO> violation : validations) {
            // template 방식
            String str = "violation = " + violation;

            if (log.isInfoEnabled()) {
                log.info("violation = " + violation);
                log.info("violation.message = " +violation.getMessage());

                // placeholder
                log.info("violation = {}", violation);
                log.info("violation.message = {}", violation.getMessage());
            }
        }

    }

}
