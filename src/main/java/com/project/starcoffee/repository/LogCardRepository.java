package com.project.starcoffee.repository;

import com.project.starcoffee.domain.card.LogCard;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LogCardRepository {

    int enrollCard(UUID memberId, UUID cardId, int cardBalance);

    List<LogCard> findByMemberId(UUID memberId);

    Optional<LogCard> findByCardId(UUID memberId, UUID cardId);

    Integer withDrawAmount(UUID cardId, long cardAmount);

    Integer findByBalance(UUID cardId);

    boolean duplicatedCard(UUID cardId);



}
