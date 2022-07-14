package com.bsks.service;

import com.bsks.api.result.Result;
import com.bsks.service.impl.OrderFallbackService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(value = "order-server",fallback = OrderFallbackService.class)
public interface OrderService {

    /**
     * 根据手机号码，产品名称查询用户已经购买的数量
     * @param phone 手机号码
     * @param productName 产品名称
     * @return 产品
     */
    @GetMapping("/service/findNumbers")
    Result findNumbers(@RequestParam("phone") String phone, @RequestParam("productName") String productName);

}
