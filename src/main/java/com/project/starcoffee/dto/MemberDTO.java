package com.project.starcoffee.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberDTO {

    @NonNull
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String tel;
    @NonNull
    private String email;
    @NonNull
    private String gender;  // 성별
    @NonNull
    private String nickName;    // 닉네임
    @NonNull
    private String loginId;     // 로그인 아이디
    @NonNull
    private String password;    // 비밀번호
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime birth;   // 생년월일


    /**
     * 회원가입 전 필수 데이터중 null 값이 있는지 검사한다.
     * null 값이 존재하여 회원가입 진행이 불가능 하다면 false 를 반환한다.
     * 검사 후 이상이 없다면 true 를 반환한다.
     * @param memberInfo
     * @return
     */
    public static boolean hasNullDataBeforeSignUp(MemberDTO memberInfo) {
        return memberInfo.getName()==null || memberInfo.getTel()==null
                || memberInfo.getEmail()==null || memberInfo.getGender()==null
                || memberInfo.getNickName() == null || memberInfo.getLoginId() == null
                || memberInfo.getPassword() == null;
    }

}
