package com.jonghae5.coinhub.service;

import com.jonghae5.coinhub.dto.CoinBuyDTO;
import com.jonghae5.coinhub.feign.BithumbFeignClient;
import com.jonghae5.coinhub.feign.response.BithumbResponse;
import com.jonghae5.coinhub.model.BithumbAssetEachStatus;
import com.jonghae5.coinhub.model.BithumbCoinPrice;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

// 항상 Disabled 처리해놓음. 에러가 날 수 있으니
//@Disabled
@SpringBootTest
@EnableFeignClients
@ExtendWith(MockitoExtension.class)
class BithumbMarketServiceIntegrationTest {

    @Autowired
    private BithumbMarketService bithumbMarketService;

   @Test
    void calculateFeeTest() throws IOException {
       Map<String, Double> result = bithumbMarketService.calculateFee();
       assertFalse(result.isEmpty());
   }
}