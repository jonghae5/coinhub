package com.jonghae5.coinhub.controller;

import com.jonghae5.coinhub.dto.TransferCalculateDTO;
import com.jonghae5.coinhub.service.TransferCalculateService;
import com.jonghae5.coinhub.view.TransferCalculateResponseView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TransferCalculateController {
//    localhost:1234/transfer-calculate?fromMarket=Bithumb&toMarket=Upbit&amount=1000000
    private final TransferCalculateService transferCalculateService;
    @GetMapping("transfer-calculate")
    public List<TransferCalculateResponseView> getPrice(
            @RequestParam String fromMarket,
            @RequestParam String toMarket,
            @RequestParam double amount
    ) throws IOException {

        return transferCalculateService.calculate(fromMarket, toMarket, amount)
                .stream()
                .map(k -> TransferCalculateResponseView.of(k, amount)).collect(Collectors.toList());

    }
}
