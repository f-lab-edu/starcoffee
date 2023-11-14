package com.project.starcoffee.mapper;

import com.project.starcoffee.domain.card.LogCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface LogCardMapper {
    int enrollCard(@Param("memberId") UUID memberId,
                   @Param("cardId") UUID cardId,
                   @Param("cardBalance") int cardBalance);
    List<LogCard> findByMemberId(UUID memberId);

    Optional<LogCard> findByCardId(UUID cardId);

    Integer withDrawAmount(@Param("cardId") UUID cardId, @Param("cardAmount") long cardAmount);

    Integer findByBalance(UUID cardId);

    boolean duplicatedCard(UUID cardId);

}
