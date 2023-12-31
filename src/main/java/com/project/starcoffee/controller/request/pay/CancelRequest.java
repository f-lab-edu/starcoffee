package com.project.starcoffee.controller.request.pay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelRequest {
    private UUID paymentId;
    @NotNull
    private UUID orderId;
    @NotNull
    private long cancelPay;
}
