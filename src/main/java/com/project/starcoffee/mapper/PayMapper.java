package com.project.starcoffee.mapper;

import com.project.starcoffee.controller.request.pay.CancelRequest;
import com.project.starcoffee.controller.response.pay.CancelResponse;
import com.project.starcoffee.dto.RequestPaySaveData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;
import java.util.UUID;

@Mapper
public interface PayMapper {
    int insertPay(RequestPaySaveData paySaveRequest);

    long findPay(UUID orderId);

    int cancelPay(CancelRequest cancelRequest);
}
