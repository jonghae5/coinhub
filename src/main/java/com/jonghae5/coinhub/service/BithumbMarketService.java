package com.jonghae5.coinhub.service;

import java.util.*;

import com.jonghae5.coinhub.dto.CoinBuyDTO;
import com.jonghae5.coinhub.dto.CoinSellDTO;
import com.jonghae5.coinhub.feign.BithumbFeignClient;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BithumbMarketService implements MarketService {

    @Value("${feeUrl.bithumb}")
    private String feeUrl;
    private final BithumbFeignClient bithumbFeignClient;
    private static final String WANNA_SELL_PRICE_KEY = "price";

    @Override
    public double getCoinCurrentPrice(String coin) {

        return Double.parseDouble(bithumbFeignClient.getCoinPrice(coin.toUpperCase() + "_KRW")
                .getData()
                .getClosing_price());
    }

    @Override
    public List<String> getCoins() {

        List<String> result = new ArrayList<>();
        bithumbFeignClient.getAssetStatus()
                .getData().forEach((k, v) -> {
                    if (v.getDeposit_status() == 1 && v.getWithdrawal_status() == 1) {
                        result.add(k.toUpperCase());
                    }
                });
        return result;
//        return List.of("A","B","C");
    }


    @Override
    public Map<String, Double> calculateFee() throws IOException {
        Map<String, Double> result = new HashMap<>();

        Document doc = Jsoup.connect(feeUrl).timeout(60000).get();
        Elements elements = doc.select("table.fee_in_out tbody tr");

        for (Element element : elements) {
            String coinHtml = element.select("td.money_type.tx_c").html();
            String coinFeeHtml = element.select("div.out_fee").html();


            if (!coinHtml.contains("(")) {
                continue;
            }
            if (coinFeeHtml.isEmpty()) {
                continue;
            }
            if (coinFeeHtml.equals("-")) {
                coinFeeHtml = "0";
            }

            coinHtml = coinHtml.substring(coinHtml.indexOf("(") + 1, coinHtml.indexOf(")"));
            result.put(coinHtml, Double.parseDouble(coinFeeHtml));
        }

        return result;


    }

    @Override
    public CoinBuyDTO calculateBuy(List<String> commonCoins, double amount) {

        Map<String, Double> amounts = new HashMap<>();
        Map<String, SortedMap<Double, Double>> orderBooks = new HashMap<>();

        // Feign으로 orderbook 가져오기
        Map<String, Object> bithumbResponse = bithumbFeignClient.getOrderBook().getData();

        // orderbook for 돌면
        bithumbResponse.forEach((k, v) -> { // key: coin, v: object
            //대소문자 구분 X
            if (!(k.equalsIgnoreCase("timestamp") || k.equalsIgnoreCase("payment_currency")
                    && commonCoins.contains(k)
            )) {
                double availableCurrency = amount;
                double availableCoin = 0;

                String coin = k;
                SortedMap<Double, Double> eachOrderBook = new TreeMap<>();
                List<Map<String, String>> wannaSell = (List<Map<String, String>>) ((Map<String, Object>) v).get("asks");

                //#1 스트림 활용한 정렬 방법
                Comparator<Map<String, String>> sortingMethod = Comparator.comparing(
                        (Map<String, String> arg) -> {
                            return Double.parseDouble(arg.get("price").toString());
                        }
                );
                wannaSell = wannaSell.stream().sorted(sortingMethod).collect(Collectors.toList());


                // 해당 호가창의 총 가격보다 큰지 작은지 비교

                for (int i = 0; i < wannaSell.size(); i++) {

                    Double price = Double.parseDouble(wannaSell.get(i).get("price"));
                    Double quantity = Double.parseDouble(wannaSell.get(i).get("quantity"));
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
                if (availableCurrency == 0) {
                    amounts.put(coin, availableCoin);
                    orderBooks.put(coin, eachOrderBook);
                }
            }
        });


        return new CoinBuyDTO(amounts, orderBooks);
    }


    public CoinSellDTO calculateSell(Map<String /*Coin Name*/, Double /*Amount*/> sellingAmounts) {
//        Map<String, Double> sellingAmounts = buyDTO.getAmounts();
        Map<String, Double> amounts = new HashMap<>();
        Map<String, SortedMap<Double, Double>> orderBooks = new HashMap<>();

        Map<String, Object> bithumbResponse = bithumbFeignClient.getOrderBook().getData();

        bithumbResponse.forEach((k, v) -> {
            if (!(k.equalsIgnoreCase("timestamp") || k.equalsIgnoreCase("payment_currency"))) {
                String coin = k;
                double sellCurrency = 0;
                Double availableCoin = sellingAmounts.get(coin);
                if (availableCoin != null) {
                    SortedMap<Double, Double> eachOrderBook = new TreeMap<>(Comparator.reverseOrder());
                    List<Map<String, String>> wannaBuy = (List<Map<String, String>>) ((Map<String, Object>) v).get("bids");

//
                    //#1 스트림 활용한 정렬 방법
                    Comparator<Map<String, String>> sortingMethod = Comparator.comparing(
                            (Map<String, String> arg) -> {
                                return Double.parseDouble(arg.get("price").toString());
                            }
                    );

                    wannaBuy = wannaBuy.stream().sorted(sortingMethod.reversed()).collect(Collectors.toList());

                    for (int i = 0; i < wannaBuy.size(); i++) {
                        Double price = Double.parseDouble(wannaBuy.get(i).get("price"));
                        Double quantity = Double.parseDouble(wannaBuy.get(i).get("quantity"));
                        Double eachTotalPrice = price * quantity;

                        // 만약 코인 양 더 많으면 끝내기
                        if (quantity >= availableCoin) { // 못넘어갈경우
                            sellCurrency += price * availableCoin;
                            eachOrderBook.put(price, availableCoin);
                            availableCoin = 0D;
                            break;
                        } else { // 다음 스텝 넘어갈경우
                            sellCurrency += eachTotalPrice;
                            eachOrderBook.put(price, quantity);
                            availableCoin -= quantity;
                        }
                    }
                    // 모두 팔지 못했을때 조건 추가 > 넣지 말기
                    if (availableCoin == 0) {
                        amounts.put(coin, sellCurrency);
                        orderBooks.put(coin, eachOrderBook);
                    }
                }
            }
        });
        return new CoinSellDTO(amounts, orderBooks);
    }


}


