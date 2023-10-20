package com.project.starcoffee.repository.mybatis;

import com.project.starcoffee.domain.card.LogCard;
import com.project.starcoffee.repository.LogCardRepository;
import com.project.starcoffee.repository.mybatis.mapper.LogCardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class LogCardRepositoryImpl implements LogCardRepository {

    private final LogCardMapper logCardMapper;

    @Autowired
    public LogCardRepositoryImpl(LogCardMapper logCardMapper) {
        this.logCardMapper = logCardMapper;
    }

    public LogCard findByCard(UUID memberId) {
        return logCardMapper.findByCard(memberId);
    }

    @Override
    public int updateAmount(UUID cardId, int balance) {
        return logCardMapper.updateAmount(cardId, balance);
    }


}
