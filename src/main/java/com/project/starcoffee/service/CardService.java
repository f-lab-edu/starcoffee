package com.project.starcoffee.service;

import com.project.starcoffee.controller.request.card.CardNickNameRequest;
import com.project.starcoffee.controller.request.card.CardNumberRequest;
import com.project.starcoffee.controller.request.card.CardRequest;
import com.project.starcoffee.domain.card.Card;
import com.project.starcoffee.repository.CardRepository;
import com.project.starcoffee.utils.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class CardService {
    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public void saveCard(CardRequest cardRequest) {
        cardRepository.saveCard(cardRequest);
    }

    public Card findByCardNumber(String cardNumber) {
        Optional<Card> cardInfo = cardRepository.findByCardNumber(cardNumber);
        cardInfo.ifPresentOrElse(card -> {},
                () -> new RuntimeException("카드 정보를 찾을 수 없습니다."));

        return cardInfo.get();
    }

    public void updateNickName(CardNickNameRequest cardInfo) {
        String cardNickName = cardInfo.getCardNickName();
        String cardNumber = cardInfo.getCardNumber();

        cardRepository.updateNickName(cardNumber, cardNickName);
    }

    public void deleteCard(CardNumberRequest cardNumber) {
        String card = cardNumber.getCardNumber();
        cardRepository.deleteCard(card);
    }

}
