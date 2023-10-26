package com.project.starcoffee.repository.mybatis.mapper;

import com.project.starcoffee.controller.request.card.CardRequest;
import com.project.starcoffee.domain.card.Card;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;
import java.util.UUID;


@Mapper
public interface CardMapper {

    int saveCard(CardRequest cardRequest);

    Optional<Card> findByCardNumber(String cardNumber);

    Optional<Card> findByCardId(UUID cardId);

    int updateNickName(@Param("cardNumber") String cardNumber, @Param("cardNickName") String cardNickName);

    int deleteCard(String cardNumber);
}
