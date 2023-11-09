package com.project.starcoffee.controller.response.pay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayResponse {
    private UUID memberId;
    private UUID orderId;
    private long storeId;
    private long orderPrice;
}
