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

    public List<LogCard> findByMemberId(UUID memberId) {
        return logCardMapper.findByMemberId(memberId);
    }

    @Override
    public Optional<LogCard> findByCardId(UUID cardId) {
        return logCardMapper.findByCardId(cardId);
    }

    @Override
    public Integer withDrawAmount(UUID cardId, long cardAmount) {
        return logCardMapper.withDrawAmount(cardId, cardAmount);
    }

    @Override
    public Integer findByBalance(UUID cardId) {
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
