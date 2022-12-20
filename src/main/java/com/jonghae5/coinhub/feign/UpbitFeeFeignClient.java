package com.jonghae5.coinhub.feign;


import com.jonghae5.coinhub.model.UpbitCoinPrice;
import com.jonghae5.coinhub.model.UpbitWithdrawalFee;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "upbitFee", url = "https://api-manager.upbit.com/api/v1/kv")
public interface UpbitFeeFeignClient {

    @GetMapping("/UPBIT_PC_COIN_DEPOSIT_AND_WITHDRAW_GUIDE")
    UpbitWithdrawalFee getWithdrawalFee();
}
