package com.project.starcoffee.repository.mybatis;

import com.project.starcoffee.controller.request.card.CardRequest;
import com.project.starcoffee.domain.card.Card;
import com.project.starcoffee.repository.CardRepository;
import com.project.starcoffee.repository.mybatis.mapper.CardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class CardRepositoryImpl implements CardRepository {

    @Autowired
    private CardMapper cardMapper;

    @Override
    public int saveCard(CardRequest cardRequest) {
        return cardMapper.saveCard(cardRequest);
    }

    @Override
    public Optional<Card> findById(String cardNumber) {
        return cardMapper.findById(cardNumber);
    }

    @Override
    public int updateNickName(String cardNumber, String cardNickName) {
        return cardMapper.updateNickName(cardNumber, cardNickName);
    }

    @Override
    public int deleteCard(String cardNumber) {
        return cardMapper.deleteCard(cardNumber);
    }


}