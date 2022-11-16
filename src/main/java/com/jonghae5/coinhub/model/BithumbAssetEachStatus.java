package com.jonghae5.coinhub.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BithumbAssetEachStatus {
    private int withdrawal_status;
    private int deposit_status;
}
