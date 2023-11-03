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
    Optional<LogCard> findByCard(UUID memberId);

    int updateAmount(UUID cardId, int cardAmount);

    int findByBalance(UUID cardId);

    int enrollCard(UUID memberId, UUID cardId, int cardBalance);

    boolean duplicatedCard(UUID cardId);



}
