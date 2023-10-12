package com.project.starcoffee.service;

import com.project.starcoffee.controller.request.card.CardNickNameRequest;
import com.project.starcoffee.controller.request.card.CardNumberRequest;
import com.project.starcoffee.controller.request.card.CardRequest;
import com.project.starcoffee.domain.card.Card;
import com.project.starcoffee.repository.CardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Optional<Card> findById(String cardNumber) {
        return cardRepository.findById(cardNumber);
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
