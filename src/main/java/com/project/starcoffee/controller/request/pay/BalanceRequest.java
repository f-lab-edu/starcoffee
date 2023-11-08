package com.project.starcoffee.controller.request.pay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceRequest {
    @NotNull
    private UUID cardId;
    @NotNull
    private int finalPrice;
}
