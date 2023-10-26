package com.project.starcoffee.dto;

import com.project.starcoffee.domain.card.CardStatus;
import com.project.starcoffee.domain.item.ItemType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
public class CardDTO {
    public UUID cardId;
    public String cardName;    // 카드 이름
    public String cardNickName;    // 카드 별칭
    public String cardNumber;  // 총 12자리(4자리씩, 하이픈 포함)
    public CardStatus status;  // 카드 상태
    public String pinNumber;   //  Pin 번호, 총 8자리
    public int cardAmount;     // 충전 및 잔액 금액
    public Timestamp createdAt;    // 카드 생성일
    public Timestamp updatedAt;    // 카드 수정일 (충전 및 결제 시 업데이트)
}
