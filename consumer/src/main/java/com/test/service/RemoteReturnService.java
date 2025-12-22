package com.test.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "return-provider")
public interface RemoteReturnService {
    @PostMapping("/return/do/{orderId}")
    boolean returnBook(@PathVariable("orderId") Long orderId);
}