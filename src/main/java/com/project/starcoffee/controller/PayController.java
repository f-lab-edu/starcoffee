package com.project.starcoffee.controller;

import com.project.starcoffee.service.PayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;
}
