package com.project.starcoffee.controller.response.pay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelResponse {
    private UUID paymentId;
    private UUID orderId;
    private long cancelPrice;
}
