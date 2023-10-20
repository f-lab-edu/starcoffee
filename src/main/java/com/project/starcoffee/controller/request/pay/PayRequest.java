package com.project.starcoffee.controller.request.pay;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.springframework.format.annotation.DateTimeFormat.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayRequest {
    private UUID memberId;
    private String storeId;
    private UUID orderId;
    private int finalPrice;
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private ZonedDateTime created_at;

}
