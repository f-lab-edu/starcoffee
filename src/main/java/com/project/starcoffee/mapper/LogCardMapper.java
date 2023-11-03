package com.project.starcoffee.mapper;

import com.project.starcoffee.domain.card.LogCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface LogCardMapper {
    Optional<LogCard> findByCard(UUID memberId);

    int updateAmount(@Param("cardId") UUID cardId, @Param("cardAmount") int cardAmount);

    int findByBalance(UUID memberId);

    int enrollCard(@Param("memberId") UUID memberId,
                   @Param("cardId") UUID cardId,
                   @Param("cardBalance") int cardBalance);

    boolean duplicatedCard(UUID cardId);

}
