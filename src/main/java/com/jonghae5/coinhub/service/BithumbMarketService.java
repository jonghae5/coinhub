package com.jonghae5.coinhub.service;


import com.jonghae5.coinhub.dto.CoinBuyDTO;
import com.jonghae5.coinhub.dto.CoinSellDTO;
import com.jonghae5.coinhub.feign.BithumbFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BithumbMarketService implements MarketService {

    private final BithumbFeignClient bithumbFeignClient;
    @Override
    public double getCoinCurrentPrice(String coin) {

        return Double.parseDouble(bithumbFeignClient.getCoinPrice(coin.toUpperCase()+"_KRW")
                .getData()
                .getClosing_price());
    }


    @Override
    public Map<String, Double> calculateFee(List<String> commonCoins, double amount) {
        return null;
    }

    @Override
    public CoinBuyDTO calculateBuy(List<String> commonCoins, double amount) {

        Map<String, Double> amounts = new HashMap<>();
        Map<String, Map<Double,Double>> orderBooks = new HashMap<>();

        // Feign으로 orderbook 가져오기
        Map<String, Object> bithumbResponse = bithumbFeignClient.getOrderBook().getData();

        // orderbook for 돌면
        bithumbResponse.forEach((k,v) -> {
            //대소문자 구분 X
            if (!(k.equalsIgnoreCase("timestamp") || k.equalsIgnoreCase("payment_currency"))) {
                double availableCurrency = amount;
                double availableCoin = 0;

                String coin = k;
                Map<Double, Double> eachOrderBook = new HashMap<>();
                List<Map<String,String>> wannaSell = (List<Map<String, String>>) ((Map<String, Object>) v).get("asks");
                // 해당 호가창의 총 가격보다 큰지 작은지 비교

                for (int i = 0; i <wannaSell.size() ; i++) {

                    Double price =  Double.parseDouble(wannaSell.get(i).get("price"));
                    Double quantity =  Double.parseDouble(wannaSell.get(i).get("quantity"));
                    Double eachTotalPrice = price * quantity;
                    Double buyableCoinAmount = availableCurrency / price;


                    // amount <= X: 현재 호가창에서 내가 살 수 있는만큼 사고 종료
                    if (availableCurrency <= eachTotalPrice) {
                        availableCoin += buyableCoinAmount;

                        eachOrderBook.put(price, buyableCoinAmount);
                        availableCurrency = 0;
                        break;
                        // amount > X : 다음 Step
                    } else {
                        availableCoin += quantity;
                        eachOrderBook.put(price, quantity);
                        availableCurrency -= eachTotalPrice;
                    }
                }
                if (availableCurrency == 0){
                    amounts.put(coin, availableCoin);
                    orderBooks.put(coin, eachOrderBook);
                }
            }
        });


        return new CoinBuyDTO(amounts, orderBooks);
    }


    @Override
    public CoinSellDTO calculateSell(CoinBuyDTO buyDTO) {


        return null;
    }

    @Override
    public List<String> getCoins() {

        List<String> result = new ArrayList<>();
        bithumbFeignClient.getAssetStatus()
                .getData().forEach((k,v) -> {
                    if(v.getDeposit_status() == 1 && v.getWithdrawal_status() == 1) {
                        result.add(k.toUpperCase());
                    }
                });
        return result;
//        return List.of("A","B","C");
    }
}


