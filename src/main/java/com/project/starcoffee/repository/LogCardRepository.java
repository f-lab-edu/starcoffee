package com.project.starcoffee.repository;

import com.project.starcoffee.domain.card.LogCard;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LogCardRepository {
    LogCard findByCard(UUID member);
    int updateAmount(UUID cardId, int balance);

}
