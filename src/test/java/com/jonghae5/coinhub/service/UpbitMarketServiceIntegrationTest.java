package com.jonghae5.coinhub.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;

// 항상 Disabled 처리해놓음. 에러가 날 수 있으니
//@Disabled
@SpringBootTest
@EnableFeignClients
@ExtendWith(MockitoExtension.class)
class UpbitMarketServiceIntegrationTest {

    @Autowired
    private UpbitMarketService upbitMarketService;

   @Test
    void calculateFeeTest() throws IOException {
       Map<String, Double> result = upbitMarketService.calculateFee();
       assertFalse(result.isEmpty());
   }
}