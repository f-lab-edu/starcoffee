package com.project.starcoffee.controller.request.card;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardNumberRequest {
    @NotNull
    private String cardNumber;

}
