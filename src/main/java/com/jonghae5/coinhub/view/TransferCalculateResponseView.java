package com.jonghae5.coinhub.view;

import com.jonghae5.coinhub.dto.TransferCalculateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Getter
@Slf4j
@AllArgsConstructor
public class TransferCalculateResponseView {
    private String coin;
    private double buyAmount;
    private double fee;
    private double sellAmount;
    private Map<Double, Double> buyOrderBook;
    private Map<Double, Double> sellOrderBook;

    private double profit;
    private double profitRatio;
    // DTO -> View
    public static TransferCalculateResponseView of(TransferCalculateDTO dto, Double input) {
        return new TransferCalculateResponseView(
                dto.getCoin(),
                dto.getBuyAmount(),
                dto.getFee(),
                dto.getSellAmount(),
                dto.getBuyOrderBook(),
                dto.getSellOrderBook(),
                dto.getSellAmount() - input,
                dto.getSellAmount() / input

        );
    }
}
