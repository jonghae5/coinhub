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
    private double mount;
    private Map<Double, Double> buOrderBook;
    private Map<Double, Double> sellOrderBook;

    // DTO -> View
    public static TransferCalculateResponseView of(TransferCalculateDTO dto) {
        return new TransferCalculateResponseView(
                dto.getCoin(),
                dto.getAmount(),
                dto.getBuyOrderBook(),
                dto.getSellOrderBook()
        );
    }
}
