package com.project.starcoffee.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.springframework.format.annotation.DateTimeFormat.*;

@Getter
@Setter
@Builder
public class MemberDTO {

    // 안붙이는게 좋을까 ? @NonNull 이라도 붙이는 게 좋을까 ?
    @NonNull
    private UUID id;

    @NotNull(message = "이름을 입력해주세요.")
    @Size(min = 2, max = 8, message = "이름을 2~8자까지 입력가능합니다.")
    private String name;

    @NotNull(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$",
            message = "휴대폰 번호 형식에 맞춰서 입력해주세요.")
    private String tel;

    @NotNull(message = "이메일 주소를 입력해주세요.")
    @Email(message = "이메일 형식을 맞춰주세요.")
    private String email;

    private String gender;  // 성별

    @NotNull(message = "닉네임은 필수값 입니다.")
    private String nickName;

    @NotNull(message = "아이디는 필수값 입니다.")
    private String loginId;

    @NotNull(message = "비밀번호는 필수값 입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$",
            message = "비밀번호는 영문, 특수문자, 숫자포함 8~20자 사이로 입력가능합니다.")
    private String password;

    @DateTimeFormat(iso = ISO.DATE_TIME)
    private ZonedDateTime birth;    // 생년월일

}
