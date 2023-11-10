package com.project.starcoffee.mapper;

import com.project.starcoffee.dto.RequestPaySaveData;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayMapper {
    int insertPay(RequestPaySaveData paySaveRequest);
}
