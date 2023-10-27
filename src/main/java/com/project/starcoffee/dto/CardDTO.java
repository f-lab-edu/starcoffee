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

    public CardDTO(UUID cardId, UUID memberId) {
        this.cardId = cardId;
        this.memberId = memberId;
    }

    public UUID cardId;
    public UUID memberId;
}
