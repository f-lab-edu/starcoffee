package com.project.starcoffee.repository;

import com.project.starcoffee.controller.request.pay.CancelRequest;
import com.project.starcoffee.controller.response.pay.CancelResponse;
import com.project.starcoffee.dto.RequestPaySaveData;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PayRepository {
    int insertPay(RequestPaySaveData paySaveRequest);

    Long findPay(UUID orderId);

    UUID findByPayment(UUID paymentId);

    int cancelPay(CancelRequest cancelRequest);

}
