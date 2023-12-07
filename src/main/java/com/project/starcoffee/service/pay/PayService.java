package com.project.starcoffee.service.pay;

import com.project.starcoffee.controller.request.pay.CancelRequest;
import com.project.starcoffee.controller.request.pay.PayRequest;
import com.project.starcoffee.controller.response.pay.CancelResponse;
import com.project.starcoffee.controller.response.pay.PayResponse;
import com.project.starcoffee.controller.response.pay.PaymentResponse;
import com.project.starcoffee.dto.OrderIdDTO;
import com.project.starcoffee.dto.RollbackRequest;
import com.project.starcoffee.dto.message.PushMessage;
import com.project.starcoffee.repository.PayRepository;
import com.project.starcoffee.kafka.LogCardProducer;
import com.project.starcoffee.kafka.OrderProducer;
import com.project.starcoffee.service.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Slf4j
@Service
public class PayService {
    private final PayRepository payRepository;
    private final PushService pushService;
    private final OrderProducer orderProducer;
    private final LogCardProducer logCardProducer;
    private final PaymentStrategy paymentStrategy;

    public PayService(PayRepository payRepository, PushService pushService,
                      OrderProducer orderProducer, LogCardProducer logCardProducer, PaymentStrategy paymentStrategy) {
        this.payRepository = payRepository;
        this.pushService = pushService;
        this.orderProducer = orderProducer;
        this.logCardProducer = logCardProducer;
        this.paymentStrategy = paymentStrategy;
    }

    // 비동기
    @Transactional
    public PaymentResponse runPay(PayRequest payRequest) {
        UUID orderId = payRequest.getOrderId();                // 주문 ID
        final long finalPrice = payRequest.getFinalPrice();    // 결제 금액
        UUID cardId = payRequest.getCardId();                  // 카드 ID
        UUID memberId = payRequest.getMemberId();              // 회원 ID
        long storeId = payRequest.getStoreId();                // 가게 ID

        try {
            // 회원카드잔액 과 결제금액의 차이 확인
            paymentStrategy.checkCardBalance(memberId, cardId, finalPrice, orderId);

            // 결제 진행 Method
            paymentStrategy.processPayment(payRequest);

            // 회원카드 금액변경
            paymentStrategy.updateCardBalance(cardId, finalPrice);

            /* 분산 트랜잭션 작동확인 */
            // throwError();

            // 회원,가게 Push 메세지
            sendPushMessages(payRequest, storeId);

            return PayResponse.builder()
                    .memberId(payRequest.getMemberId())
                    .orderId(payRequest.getOrderId())
                    .storeId(payRequest.getStoreId())
                    .finalPrice(finalPrice)
                    .build();

        } catch (Exception e) {
            log.error("결제처리 중에 오류가 발생하였습니다. : {}", e.getMessage());

            // saga 패턴(choreography) - kafka
            CancelResponse cancelResponse = handlePaymentFailure(orderId, finalPrice, cardId);
            return cancelResponse;
        }
    }

    /* 고객에게 푸시 알림 ("음료가 준비중입니다."), 가게에 푸시 알림 ("음료를 준비해주세요.") */
    private void sendPushMessages(PayRequest payRequest, long storeId) {
        PushMessage memberCompleteMsg = PushMessage.MEMBER_PAYMENT_COMPLETE;
        // pushService.sendByMember(memberCompleteMsg, payRequest.getMemberId().toString());
        PushMessage storeCompleteMsg = PushMessage.STORE_PAYMENT_COMPLETE;
        // pushService.sendByStore(storeCompleteMsg, storeId);
    }

    public CancelResponse handlePaymentFailure(UUID orderId, long finalPrice, UUID cardId) {
        // 결제 테이블 롤백
        CancelRequest cancelRequest = CancelRequest.builder()
                .orderId(orderId)
                .cancelPay(finalPrice)
                .build();
        CancelResponse cancelResponse = runCancel(cancelRequest);

        // order-rollback(kafka) 요청
        OrderIdDTO orderIdDTO = OrderIdDTO.builder()
                .orderId(orderId)
                .build();
        orderProducer.rollbackOrder(orderIdDTO);

        // logcard-rollback(kafka) 요청
        RollbackRequest rollbackRequest = RollbackRequest.builder()
                .cardId(cardId)
                .finalPrice(finalPrice)
                .build();
        logCardProducer.rollbackLogcard(rollbackRequest);

        return cancelResponse;
    }

    @Transactional
    public CancelResponse runCancel(CancelRequest cancelRequest) {
        UUID orderId = cancelRequest.getOrderId();
        // 결제 테이블에서 결제금액 확인
        Long cancelPay = payRepository.findPay(orderId);

        // 주문건이 없는 경우나, 환불요청을 이미 했을 경우를 확인
        if (cancelPay == null) {
            throw new RuntimeException("이미 환불되었거나 주문요청을 찾을 수 없습니다.");
        }

        // 결제 테이블에서 환불정보 INSERT
        int result = payRepository.cancelPay(cancelRequest);
        if (result != 1) {
            throw new RuntimeException("결제취소가 완료되지 못했습니다.");
        }

        return CancelResponse.builder()
                .paymentId(cancelRequest.getPaymentId())
                .orderId(orderId)
                .finalPrice(-cancelPay)
                .build();
    }

    public static void throwError() {
        throw new RuntimeException("결제중 오류발생!!!!!!!!!!!!!!!!!!");
    }

}
