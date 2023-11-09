package com.project.starcoffee.repository;

import com.project.starcoffee.domain.card.Card;
import com.project.starcoffee.domain.card.LogCard;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LogCardRepository {

    int enrollCard(UUID memberId, UUID cardId, int cardBalance);

    Optional<LogCard> findByCard(UUID memberId);

    Integer withDrawAmount(UUID cardId, long cardAmount);

    Integer findByBalance(UUID cardId);

    boolean duplicatedCard(UUID cardId);



}
