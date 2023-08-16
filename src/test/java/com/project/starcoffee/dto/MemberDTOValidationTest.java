package com.project.starcoffee.dto;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
public class MemberDTOValidationTest {

    private MemberDTO member;

    @BeforeEach
    void init() {
        member = MemberDTO.builder()
                .id(1L)
                .name("김")
                .tel("010-12-1234")
                .email("testnaver.com")
                .gender("M")
                .nickName("nickName")
                .loginId("testId")
                .password("1234")
                .birth(LocalDateTime.now())
                .build();
    }

    @Test
    void memberValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<MemberDTO>> validations = validator.validate(member);

        for (ConstraintViolation<MemberDTO> violation : validations) {
            log.info("violation = " + violation);
            log.info("violation.message = " +violation.getMessage());
        }
    }

}
