package com.project.starcoffee.domain.card;

import java.time.LocalDateTime;

public class Card {

    private Long id;
    private String loginId;     // 로그인 아이디
    private String cardName;
    private String serialNumber;  // 총 12자리(4자리씩)
    private String pinNumber;    //  Pin 번호, 총 8자리
    private String country;     // 사용가능한 나라
    private int amount;     // 충전 및 잔액 금액
    private LocalDateTime validated;    // 충전금액 유효기간 (최종충전 및 사용일로부터 5년)
    private UsageCard usageCard;    // 카드 이용내역
    private GeneralCharge generalCharge; // 일반 충전
    private String barCode;     //////// 바코드처리는 어떻게 하지 ?

}
