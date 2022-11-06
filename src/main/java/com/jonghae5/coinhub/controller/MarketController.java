package com.jonghae5.coinhub.controller;


import com.jonghae5.coinhub.service.BithumbMarketService;
import com.jonghae5.coinhub.service.CommonMarketService;
import com.jonghae5.coinhub.service.MarketService;
import com.jonghae5.coinhub.service.UpbitMarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MarketController {

    private CommonMarketService commonMarketService;
    /**
     * 코인의 최근 가격 : 어떤 마켓인지, 어떤 코인인지
     * @return
     */
    @GetMapping("/price")
    public double getPrice(
            @RequestParam String market,
            @RequestParam String coin
    ) {
        return commonMarketService.getPrice(market, coin);
    }
}
