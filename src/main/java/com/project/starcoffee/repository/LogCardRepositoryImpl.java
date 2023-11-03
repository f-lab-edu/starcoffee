package com.project.starcoffee.repository;

import com.project.starcoffee.domain.card.LogCard;
import com.project.starcoffee.mapper.LogCardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class LogCardRepositoryImpl implements LogCardRepository {

    private final LogCardMapper logCardMapper;

    @Autowired
    public LogCardRepositoryImpl(LogCardMapper logCardMapper) {
        this.logCardMapper = logCardMapper;
    }

    public Optional<LogCard> findByCard(UUID memberId) {
        return logCardMapper.findByCard(memberId);
    }

    @Override
    public int updateAmount(UUID cardId, int cardAmount) {
        return logCardMapper.updateAmount(cardId, cardAmount);
    }

    @Override
    public int findByBalance(UUID cardId) {
        return logCardMapper.findByBalance(cardId);
    }

    @Override
    public int enrollCard(UUID memberId, UUID cardId, int cardBalance) {
        return logCardMapper.enrollCard(memberId, cardId, cardBalance);
    }

    @Override
    public boolean duplicatedCard(UUID cardId) {
        return logCardMapper.duplicatedCard(cardId);
    }



}
