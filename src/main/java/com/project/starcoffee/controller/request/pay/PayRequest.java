package com.project.starcoffee.controller.request.pay;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.springframework.format.annotation.DateTimeFormat.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayRequest {
    @NotNull
    private UUID memberId;
    @NotNull
    private UUID cardId;
    @NotNull
    private UUID orderId;
    @NotNull
    private long storeId;
    @NotNull
    private int finalPrice;
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private Timestamp created_at;

}
