package com.project.starcoffee.repository;

import com.project.starcoffee.dto.RequestPaySaveData;
import org.springframework.stereotype.Repository;

@Repository
public interface PayRepository {
    int insertPay(RequestPaySaveData paySaveRequest);
}
