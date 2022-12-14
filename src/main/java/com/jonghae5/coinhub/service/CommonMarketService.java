package com.jonghae5.coinhub.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommonMarketService {
    private final Map<String, MarketService> marketServices;

    public double getPrice(String market, String coin) {
        MarketService marketService = getMarketService(marketServices, market);

        return marketService.getCoinCurrentPrice(coin);
    }

    public static MarketService getMarketService(Map<String, MarketService> marketServices, String market) {
        log.info("market={}", market);
        log.info("marketServices.keySet()");
        for(String key : marketServices.keySet()) {
            if(key.substring(0, market.length()).equals(market.toLowerCase())) {
                return marketServices.get(key);
            }
        }
        return null;
    }

    public List<String> getCommonCoin(String fromMarket, String toMarket) {
        //TODO
        // 코인을 두 마켓 모두 호환할 수 있는지 확인

        MarketService fromMarketService = getMarketService(marketServices, fromMarket);
        MarketService toMarketService = getMarketService(marketServices, toMarket);

        List<String> fromMarketCoins = fromMarketService.getCoins();
        List<String> toMarketCoins = toMarketService.getCoins();

        List<String> result = new ArrayList<>();
        for (String fromMarketCoin : fromMarketCoins) {
            if (toMarketCoins.contains(fromMarketCoin)) {
                result.add(fromMarketCoin);
            }
        }
        return result;
    }
}