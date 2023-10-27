package com.project.starcoffee.service;

import com.project.starcoffee.domain.card.Card;
import com.project.starcoffee.domain.card.LogCard;
import com.project.starcoffee.repository.CardRepository;
import com.project.starcoffee.repository.LogCardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class LogCardService {
    private final LogCardRepository logCardRepository;

    @Autowired
    public LogCardService(LogCardRepository logCardRepository) {
        this.logCardRepository = logCardRepository;
    }

    public LogCard findByCard(String member) {
        UUID memberId = UUID.fromString(member);
        Optional<LogCard> cardInfoOptional = logCardRepository.findByCard(memberId);

        cardInfoOptional.ifPresentOrElse(card -> {},
                ()-> { throw new RuntimeException("회원으로 등록된 카드를 찾을 수 없습니다."); }
        );

        return cardInfoOptional.get();
    }
}
