package com.project.starcoffee.domain.card;

public class GeneralCharge {

    /**
     * 충전금액
        * 1만원, 3만원, 5만원, 10만원
     */
    public enum Amount {
        ONE, THREE, FIVE, TEN
    }

    /**
     * 결제수단
        * 신용카드, SSGPAY
     */
    public enum PaymentMethod {
       CREDIT_CARD, SSG_PAY
    }

    private Amount amount;

    private PaymentMethod paymentMethod;


}
