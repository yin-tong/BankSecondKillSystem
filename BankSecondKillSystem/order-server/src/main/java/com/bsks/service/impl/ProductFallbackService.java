package com.bsks.service.impl;

import com.bsks.api.result.Result;
import com.bsks.api.result.ResultCode;
import com.bsks.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductFallbackService implements ProductService {

    @Override
    public Result reduceProduct(String productName, int number) {
        return new Result(ResultCode.ProductServerError);
    }

    @Override
    public Result reduceProductQuantity(long productId, int number) {
        return new Result(ResultCode.ProductServerError);
    }
}
