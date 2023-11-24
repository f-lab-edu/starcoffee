package com.project.starcoffee.dto;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderIDDTO {
    private UUID orderId;
}
