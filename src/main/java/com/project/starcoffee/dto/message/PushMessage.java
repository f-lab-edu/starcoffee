package com.project.starcoffee.dto.message;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
public class PushMessage {

    @NotNull
    private String title;
    @NotNull
    private String message;
    private Timestamp generatedTime;

    public static final PushMessage STORE_PAYMENT_COMPLETE = new PushMessage("결제완료", "음료를 준비해주세요.");
    public static final PushMessage CANCEL_ORDER_REQUEST = new PushMessage("주문취소", "매장에서 주문을 취소하였습니다.");
    public static final PushMessage ORDER_COMPLETE = new PushMessage("결제완료", "음료가 준비중입니다.");
    public static final PushMessage BEVERAGE_COMPLETE = new PushMessage("음료완료", "음료가 완성되었습니다.");


    public PushMessage(String title, String message) {
        this.title = title;
        this.message = message;
        this.generatedTime = Timestamp.valueOf(LocalDateTime.now());
    }
}
