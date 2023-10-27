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
public class PayResponse {
    private UUID memberId;
    private String storeId;
    private int orderPrice;
}
