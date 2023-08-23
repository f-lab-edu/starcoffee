package com.project.starcoffee.domain.member;

import com.project.starcoffee.domain.card.Card;
import com.project.starcoffee.repository.MemberRepository;
import com.project.starcoffee.utils.SHA256Util;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public class Member {

    private MemberRepository memberRepository;

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


    /**
     * 도메인 로직으로 이전 비밀번호가 맞는지 확인한다.
     * @param id 비밀변경 할 loginId
     * @param beforePw 변경 전 비밀번호
     * @param afterPw 변경 하고자 하는 비밀번호
     */
    public String matchesPasswordChangePassword(String id, String beforePw, String afterPw) {
        String encryptPassword = SHA256Util.encryptSHA256(beforePw);

        if (memberRepository.findByIdAndPassword(id, encryptPassword) == null) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return SHA256Util.encryptSHA256(afterPw);
    }


}
