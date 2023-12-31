package com.project.starcoffee.dto;

import com.project.starcoffee.domain.pay.PayType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestPayData {
    @NotNull
    private UUID orderId;
    @NotNull
    private UUID cardId;
}
