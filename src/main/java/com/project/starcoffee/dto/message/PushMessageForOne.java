package com.project.starcoffee.dto.message;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PushMessageForOne extends PushMessage {

    @NotNull
    private String token;

    public PushMessageForOne(String title, String message, String token) {
        super(title, message);
        this.token = token;
    }
}
