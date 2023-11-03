package com.project.starcoffee.controller;

import com.project.starcoffee.controller.request.card.CardNumberRequest;
import com.project.starcoffee.domain.card.Card;
import com.project.starcoffee.domain.card.LogCard;
import com.project.starcoffee.service.LogCardService;
import com.project.starcoffee.utils.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping("/logcard")
public class LogCardController {

    private final LogCardService logCardService;

    @Autowired
    public LogCardController(LogCardService logCardService) {
        this.logCardService = logCardService;
    }

    /**
     * 회원의 아이디로 카드등록을 한다.
     * @param cardNumberRequest 카드정보
     * @param session 세션
     * @return
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Card> enrollCard(@RequestBody CardNumberRequest cardNumberRequest, HttpSession session) {
        Card card = logCardService.enrollCard(cardNumberRequest, session);

        return Stream.of(card)
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public LogCard findCard(HttpSession session) {
        String memberId = SessionUtil.getMemberId(session);
        return logCardService.findByCard(memberId);
    }




}
