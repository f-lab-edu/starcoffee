package com.project.starcoffee.domain.member;

import com.project.starcoffee.domain.card.Card;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
public class Member {

    @NonNull
    private Long id;
    @NonNull
    private String name;
    private String loginId;
    private String password;
    @NonNull
    private String tel;
    @NonNull
    private String email;
    private MemberStatus status;  //   회원상태
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime birth;   // 생년월일
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;  // 가입일
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;  // 수정일
    @NonNull
    private String gender;  // 성별
    @NonNull
    private String nickName;    // 닉네임
    private Card card;  // 스타커피 카드정보

}
