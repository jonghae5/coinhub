package com.jonghae5.coinhub.service;


import com.jonghae5.coinhub.dto.CoinBuyDTO;
import com.jonghae5.coinhub.dto.CoinSellDTO;
import com.jonghae5.coinhub.feign.BithumbeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BithumbMarketService implements MarketService {

    private final BithumbeignClient bithumbeignClient;
    @Override
    public double getCoinCurrentPrice(String coin) {

        return Double.parseDouble(bithumbeignClient.getCoinPrice(coin.toUpperCase()+"_KRW")
                .getData()
                .getClosing_price());
    }

    @Override
    public CoinBuyDTO calculateBuy(List<String> commonCoins, double amount) {
        return null;
    }

    @Override
    public Map<String, Double> calculateFee(List<String> commonCoins, double amount) {
        return null;
    }

    @Override
    public CoinSellDTO calculateSell(List<String> commonCoins, double amount) {
        return null;
    }
}


