package com.project.starcoffee.repository;

import com.project.starcoffee.controller.request.card.CardRequest;
import com.project.starcoffee.domain.card.Card;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;
import java.util.UUID;

public interface CardRepository {
    int saveCard(CardRequest cardRequest);

    Optional<Card> findById(String cardNumber);

    int updateNickName(String cardNumber, String cardNickName);

    int deleteCard(String cardNumber);
}
