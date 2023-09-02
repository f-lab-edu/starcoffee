package com.project.starcoffee.domain.card;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class Card {

    private UUID cardId;
    private String loginId;     // 로그인 아이디
    private String cardName;    // 카드 이름
    private String cardNickName;    // 카드 별칭
    private String cardNumber;  // 총 12자리(4자리씩, 하이픈 포함)
    private String pinNumber;   //  Pin 번호, 총 8자리
    private int CardAmount;     // 충전 및 잔액 금액
    private Timestamp createdAt;    // 카드 생성일
    private Timestamp updatedAt;    // 카드 수정일 (충전 및 결제 시 업데이트)

    /*
    결제 이력 내역 추가 예정
     */

}
