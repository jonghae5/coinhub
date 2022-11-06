package com.jonghae5.coinhub.feign;

import com.jonghae5.coinhub.feign.response.BithumbResponse;
import com.jonghae5.coinhub.model.BithumbCoinPrice;
import com.jonghae5.coinhub.model.UpbitCoinPrice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "bithumb", url = "https://api.bithumb.com/public")
public interface BithumbeignClient {

    @GetMapping("/ticker/{coin}")
    BithumbResponse<BithumbCoinPrice> getCoinPrice(@PathVariable("markets") String coin);
}
