package com.jonghae5.coinhub.service;

import com.jonghae5.coinhub.dto.CoinBuyDTO;
import com.jonghae5.coinhub.dto.CoinSellDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface MarketService {

    double getCoinCurrentPrice(String coin);

    CoinBuyDTO calculateBuy(List<String> commonCoins, double amount);

    CoinSellDTO calculateSell(Map<String /*Coin Name*/, Double /*Amount*/> amounts);

    Map<String /*Coin Name*/, Double /*Withdrawal Fee */> calculateFee() throws IOException;

    List<String> getCoins();
}
