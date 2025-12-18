package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients // [关键] 开启 Feign 远程调用功能
public class ConApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConApplication.class, args);
    }
}