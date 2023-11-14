package com.project.starcoffee.dto;

import com.project.starcoffee.domain.pay.PayStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPaySaveData {
    private UUID orderId;
    private long finalPrice;
    private PayStatus status;
}
