package com.project.starcoffee.repository;

import com.project.starcoffee.mapper.PayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PayRepositoryImpl implements PayRepository {

    private final PayMapper payMapper;

    @Autowired
    public PayRepositoryImpl(PayMapper payMapper) {
        this.payMapper = payMapper;
    }


}
