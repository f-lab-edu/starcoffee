package com.project.starcoffee.controller;

import com.project.starcoffee.aop.session.SessionMemberId;
import com.project.starcoffee.service.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/push")
public class PushController {

    private final PushService pushService;

    @Autowired
    public PushController(PushService pushService) {
        this.pushService = pushService;
    }

    @PostMapping("/register")
    @SessionMemberId
    public ResponseEntity registerToken(@RequestBody String token, String memberId) {
        log.error("token = {}", token);
        pushService.addMemberToken(memberId, token);
        return ResponseEntity.ok().build();
    }
}
