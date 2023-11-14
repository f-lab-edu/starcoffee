package com.project.starcoffee.repository;

import com.project.starcoffee.controller.request.card.CardRequest;
import com.project.starcoffee.domain.card.Card;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository {
    int saveCard(CardRequest cardRequest);

    Optional<Card> findByCardNumber(String cardNumber);

    Optional<Card> findByCardId(UUID cardId);

    Optional<Card> findCard(String cardNumber, String pinNumber);

    int updateNickName(String cardNumber, String cardNickName);

    int deleteCard(String cardNumber);


}
