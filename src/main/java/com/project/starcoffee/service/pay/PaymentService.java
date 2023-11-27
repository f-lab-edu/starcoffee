package com.project.starcoffee.service.pay;

import com.project.starcoffee.controller.request.pay.PayRequest;
import com.project.starcoffee.controller.response.pay.PayResponse;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {
    PayResponse runPay(PayRequest payRequest);
}
