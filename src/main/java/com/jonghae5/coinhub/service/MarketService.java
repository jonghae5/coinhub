package com.jonghae5.coinhub.service;

import com.jonghae5.coinhub.dto.CoinBuyDTO;
import com.jonghae5.coinhub.dto.CoinSellDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


public interface MarketService {

    double getCoinCurrentPrice(String coin);

    CoinBuyDTO calculateBuy(List<String> commonCoins, double amount);

    Map<String, Double> calculateFee(List<String> commonCoins, double amount);

    CoinSellDTO calculateSell(List<String> commonCoins, double amount);

    List<String> getCoins();
}
