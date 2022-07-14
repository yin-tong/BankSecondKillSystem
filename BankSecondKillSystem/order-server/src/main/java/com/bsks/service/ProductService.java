package com.bsks.service;

import com.bsks.api.result.Result;
import com.bsks.service.impl.ProductFallbackService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(value = "product-server",fallback = ProductFallbackService.class)
public interface ProductService {

    /**
     * 产品数量减number（数量不能小于0)
     * @param productName 产品名称
     * @param number 减少的数量
     */
    @GetMapping("/service/reduceProduct")
    Result reduceProduct(@RequestParam("productName") String productName, @RequestParam("number")int number);

    /**
     * 从redis中扣减库存
     * @param productId 产品id
     * @param number 扣减数量
     * @return
     */
    @GetMapping("/service/reduceProductQuantity")
    public Result reduceProductQuantity(@RequestParam("productId")long productId,@RequestParam("number") int number);
}
