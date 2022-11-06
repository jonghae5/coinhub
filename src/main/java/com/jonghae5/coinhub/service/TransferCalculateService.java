package com.jonghae5.coinhub.service;

import com.jonghae5.coinhub.dto.CoinBuyDTO;
import com.jonghae5.coinhub.dto.CoinSellDTO;
import com.jonghae5.coinhub.dto.TransferCalculateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferCalculateService {
    private final CommonMarketService commonMarketService;
    private final Map<String, MarketService> marketServices;

    public TransferCalculateDTO calculate(String fromMarket, String toMarket, double amount) {

        // from, to : common coin
        List<String> commonCoins = commonMarketService.getCommonCoin(fromMarket, toMarket);

        MarketService fromMarketService = CommonMarketService.getMarketService(marketServices, fromMarket);
        MarketService toMarketService = CommonMarketService.getMarketService(marketServices, toMarket);

        // from 얼마에 살수있는지
        CoinBuyDTO fromMarketBuyDto = fromMarketService.calculateBuy(commonCoins, amount);
        // from 이체 수수료
        Map<String, Double> fromMarketTransferFee = fromMarketService.calculateFee(commonCoins, amount);
        // to 얼마에 팔수 있는지
        CoinSellDTO toMarketSellDto = toMarketService.calculateSell(commonCoins, amount);

        //TODO
        // 가장 높은 값을 받을수있는 코인을 선택
//        String transferCoin = toMarketSellDto.getAmounts().keySet().get(0);
        String transferCoin = "ETC";
        return new TransferCalculateDTO(
                transferCoin,
                toMarketSellDto.getAmounts().get(transferCoin),
                fromMarketBuyDto.getOrderBooks().get(transferCoin),
                toMarketSellDto.getOrderBooks().get(transferCoin)
        );
    }
}