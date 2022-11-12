package com.jonghae5.coinhub.feign;

import com.jonghae5.coinhub.feign.response.BithumbResponse;
import com.jonghae5.coinhub.model.BithumbCoinPrice;
import com.jonghae5.coinhub.model.BithumbAssetEachStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "bithumb", url = "https://api.bithumb.com/public")
public interface BithumbeignClient {

    @GetMapping("/ticker/{coin}")
    BithumbResponse<BithumbCoinPrice> getCoinPrice(@PathVariable("markets") String coin);

    @GetMapping("/assetsstatus/ALL")
    BithumbResponse<Map<String,BithumbAssetEachStatus>> getAssetStatus();
}
