package com.project.starcoffee.service;

import com.project.starcoffee.domain.card.Card;
import com.project.starcoffee.domain.card.LogCard;
import com.project.starcoffee.repository.CardRepository;
import com.project.starcoffee.repository.LogCardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class LogCardService {

    private final LogCardRepository logCardRepository;

    private final CardRepository cardRepository;

    @Autowired
    public LogCardService(LogCardRepository logCardRepository, CardRepository cardRepository) {
        this.logCardRepository = logCardRepository;
        this.cardRepository = cardRepository;
    }

    public Card findCard(String memberId) {
        UUID member = UUID.fromString(memberId);
        LogCard logCard = logCardRepository.findByCard(member);

        UUID cardId = logCard.getCardId();
        Card cardInfo = cardRepository.findByCard(cardId);

        return cardInfo;
    }
}
