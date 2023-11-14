package com.project.starcoffee.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class PushMessageForOne extends PushMessage {

    @NotNull
    private String token;

    @Builder
    public PushMessageForOne(String title, String message, String token) {
        super(title, message);
        this.token = token;
    }
}
