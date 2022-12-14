package com.jonghae5.coinhub.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.jonghae5.coinhub.dto.CoinBuyDTO;
import com.jonghae5.coinhub.dto.CoinSellDTO;
import com.jonghae5.coinhub.feign.UpbitFeeFeignClient;
import com.jonghae5.coinhub.feign.UpbitFeignClient;
import com.jonghae5.coinhub.model.UpbitEachWithdrawalFee;
import com.jonghae5.coinhub.model.UpbitOrderBooks;
import com.jonghae5.coinhub.model.UpbitWithdrawalFee;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpbitMarketService implements MarketService {

    private final UpbitFeeFeignClient upbitFeeFeignClient;
    private final UpbitFeignClient upbitFeignClient;
    @Override
    public double getCoinCurrentPrice(String coin) {
        return upbitFeignClient.getCoinPrice("KRW-" + coin.toUpperCase())
                .get(0)
                .getTrade_price();
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

    //TODO
    @Override
    public CoinBuyDTO calculateBuy(List<String> commonCoins, double amount) {
        Map<String, Double> amounts = new HashMap<>();
        Map<String, SortedMap<Double, Double>> orderBooks = new HashMap<>();
        commonCoins.stream().map(k -> "KRW-" + k.toUpperCase()).collect(Collectors.toList());

        List<UpbitOrderBooks> bithumbResponse = upbitFeignClient.getOrderBooks(commonCoins);
        bithumbResponse.forEach(k -> {
            double availableCurrency = amount;
            double availableCoin = 0;
            String coin = k.getMarket().substring(4);
            SortedMap<Double, Double> eachOrderBook = new TreeMap<>();

            List<UpbitOrderBooks.UpbitEachOrderBooks> eachOrderBooks = k.getOrderbook_units();
            eachOrderBooks.sort(Comparator.comparingDouble(UpbitOrderBooks.UpbitEachOrderBooks::getAsk_price)); // ask_price ?????? ????????????,
            for(int i=0; i<eachOrderBooks.size(); i++) {
                Double price = eachOrderBooks.get(i).getAsk_price();
                Double quantity = eachOrderBooks.get(i).getAsk_size();
                Double eachTotalPrice = price * quantity;
                double buyableCoinAmount = availableCurrency/price;

                // ?????? ?????? ????????? ????????????,????????? ?????????
                if(eachTotalPrice >= availableCurrency) { // ??????????????????
                    availableCoin += buyableCoinAmount;
                    eachOrderBook.put(price, buyableCoinAmount);
                    availableCurrency = 0;
                    break;
                } else { // ?????? ?????? ???????????????
                    availableCoin += quantity;
                    eachOrderBook.put(price, quantity);
                    availableCurrency -= price * quantity;
                }
            }

            // ?????? ?????? ????????? ???????????? ?????? ?????? > ?????? ??????
            if(availableCurrency == 0) {
                amounts.put(coin, availableCoin);
                orderBooks.put(coin, eachOrderBook);
            }
        });

        return new CoinBuyDTO(amounts, orderBooks);
    }


    //TODO
    @Override
    public CoinSellDTO calculateSell(Map<String /*Coin Name*/, Double /*Amount*/> sellingAmounts) {
//        Map<String, Double> sellingAmounts = buyDTO.getAmounts();
        Map<String, Double> amounts = new HashMap<>();
        Map<String, SortedMap<Double, Double>> orderBooks = new HashMap<>();
        List<String> coins = sellingAmounts.keySet().stream().map(k -> "KRW-" + k.toUpperCase()).collect(Collectors.toList());

        List<UpbitOrderBooks> upbitResponse = upbitFeignClient.getOrderBooks(coins);

        upbitResponse.forEach(k -> {
            String coin = k.getMarket().substring(4);
            double sellCurrency = 0;
            Double availableCoin = sellingAmounts.get(coin);

            if(availableCoin != null) {
                SortedMap<Double, Double> eachOrderBook = new TreeMap<>(Comparator.reverseOrder());
                List<UpbitOrderBooks.UpbitEachOrderBooks> eachOrderBooks = k.getOrderbook_units();
                eachOrderBooks.sort(Comparator.comparingDouble(UpbitOrderBooks.UpbitEachOrderBooks::getBid_price).reversed()); // bid_price ?????? ????????????,
                for(int i=0; i<eachOrderBooks.size(); i++) {
                    Double price = eachOrderBooks.get(i).getBid_price();
                    Double quantity = eachOrderBooks.get(i).getBid_size();
                    Double eachTotalPrice = price * quantity;

                    // ?????? ?????? ??? ??? ????????? ?????????
                    if(quantity >= availableCoin) { // ??????????????????
                        sellCurrency += price * availableCoin;
                        eachOrderBook.put(price, availableCoin);
                        availableCoin = 0D;
                        break;
                    } else { // ?????? ?????? ???????????????
                        sellCurrency += eachTotalPrice;
                        eachOrderBook.put(price, quantity);
                        availableCoin -= quantity;
                    }
                }
                // ?????? ?????? ???????????? ?????? ?????? > ?????? ??????
                if(availableCoin == 0) {
                    amounts.put(coin, sellCurrency);
                    orderBooks.put(coin, eachOrderBook);
                }
            }
        });

        return new CoinSellDTO(amounts, orderBooks);
    }

    @Override
    public Map<String, Double> calculateFee() throws JsonProcessingException {
        Map<String, Double> result = new HashMap<>();

        return upbitFeeFeignClient.getWithdrawalFee().getData().stream()
                .collect(Collectors.toMap(
                        UpbitEachWithdrawalFee::getCurrency,
                        UpbitEachWithdrawalFee::getWithdrawFee
                ));
    }


}
