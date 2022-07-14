package com.bsks.service.impl;

import com.bsks.api.result.Result;
import com.bsks.api.result.ResultCode;
import com.bsks.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductFallbackService implements ProductService {

    /**
     * 根据产品id查询产品信息,兜底方法
     * @param productId 产品id
     * @return 产品
     */
    @Override
    public Result findProductById(long productId) {
        return  new Result(ResultCode.ProductServerError);
    }
}
