package com.project.starcoffee.dto;

import com.project.starcoffee.domain.card.CardStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberCardDTO {
    @NotNull
    private UUID cardId;
    @NotNull
    private CardStatus status;
    @NotNull
    private int cardBalance;

}
