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
     * 根据产品id查询产品信息
     * @param productId 产品id
     * @return 产品
     */
    @GetMapping("/service/findProductById")
    Result findProductById(@RequestParam("productId") long productId);

}
