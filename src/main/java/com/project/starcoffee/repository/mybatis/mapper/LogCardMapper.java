package com.project.starcoffee.repository.mybatis.mapper;

import com.project.starcoffee.domain.card.LogCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.UUID;

@Mapper
public interface LogCardMapper {
    LogCard findByCard(UUID member);

    int updateAmount(@Param("cardId") UUID cardId, @Param("balance") int balance);
}
