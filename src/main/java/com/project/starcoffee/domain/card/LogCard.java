package com.project.starcoffee.domain.card;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class LogCard {

    private UUID cardLogId;
    private UUID memberId;
    private UUID cardId;
    private CardStatus status;
    private MoneyStatus moneyStatus;
    private int cardAmount;
    private int cardBalance;
    private Timestamp createdAt;
    private Timestamp updatedAt;

}
