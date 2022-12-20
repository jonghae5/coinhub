package com.jonghae5.coinhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;

@EnableFeignClients
@SpringBootApplication
public class CoinhubApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoinhubApplication.class, args);
	}

}

