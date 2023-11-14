package com.project.starcoffee.domain.card;

import lombok.*;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
