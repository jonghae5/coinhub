package com.jonghae5.coinhub.controller;

import com.jonghae5.coinhub.dto.TransferCalculateDTO;
import com.jonghae5.coinhub.service.TransferCalculateService;
import com.jonghae5.coinhub.view.TransferCalculateResponseView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TransferCalculateController {

    private final TransferCalculateService transferCalculateService;
    @GetMapping("transfer-calculate")
    public TransferCalculateDTO getPrice(
            @RequestParam String fromMarket,
            @RequestParam String toMarket,
            @RequestParam double amount
    ) {

        return transferCalculateService.calculate(fromMarket, toMarket, amount);
    }
}
