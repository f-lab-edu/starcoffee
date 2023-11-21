package com.project.starcoffee.domain.member;

import com.project.starcoffee.domain.card.LogCard;
import lombok.*;

import com.project.starcoffee.utils.SHA256Util;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
public class Member {

    private UUID memberId;

    private String name;

    private String loginId;

    private String password;

    private String tel;

    private String email;

    private MemberStatus status;  //   회원 상태

    private Timestamp birth;   // 생년 월일

    private Timestamp createdAt;  // 가입일

    private Timestamp updatedAt;  // 수정일

    private String nickName;    // 닉네임

    private String gender;  // 성별

     private List<LogCard> cards;  // 스타커피 카드정보


    /**
     * 이전 비밀번호가 맞는지 확인한다.
     * @param beforePw 변경 전 비밀번호
     * @param afterPw 변경 하고자 하는 비밀번호
     */
    public String matchesAndChangePassword(String memberPassword, String beforePw, String afterPw) {

        if (!memberPassword.equals(beforePw)) {
            throw new IllegalArgumentException("현재 비밀번호가 맞지않습니다.");
        }

        return SHA256Util.encryptSHA256(afterPw);
    }
}
