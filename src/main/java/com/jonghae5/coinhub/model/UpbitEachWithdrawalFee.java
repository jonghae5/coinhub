package com.jonghae5.coinhub.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpbitEachWithdrawalFee {
    private String currency;
    private Double withdrawFee;
}
