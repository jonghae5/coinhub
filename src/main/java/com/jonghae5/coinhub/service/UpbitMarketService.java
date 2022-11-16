package com.jonghae5.coinhub.service;


import com.jonghae5.coinhub.dto.CoinBuyDTO;
import com.jonghae5.coinhub.dto.CoinSellDTO;
import com.jonghae5.coinhub.feign.UpbitFeignClient;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpbitMarketService implements MarketService {

    private final UpbitFeignClient upbitFeignClient;
    @Override
    public double getCoinCurrentPrice(String coin) {
        return upbitFeignClient.getCoinPrice("KRW-" + coin.toUpperCase())
                .get(0)
                .getTrade_price();
    }

    //TODO
    @Override
    public CoinBuyDTO calculateBuy(List<String> commonCoins, double amount) {
        return null;
    }
    //TODO
    @Override
    public Map<String, Double> calculateFee(List<String> commonCoins, double amount) {
        return null;
    }
    //TODO
    @Override
    public CoinSellDTO calculateSell(CoinBuyDTO buyDTO) {
        return null;
    }

    @Override
    public List<String> getCoins() {
//        return List.of("A","B","D");

        List<String> result = new ArrayList<>();
        upbitFeignClient.getMarketCode().forEach(x -> {
            if(x.getMarket().startsWith("KRW")) {
                result.add(x.getMarket().substring(4).toUpperCase());
            }
        });
        return result;
    }
}
