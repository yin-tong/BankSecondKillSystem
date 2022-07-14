package com.bsks.service;

import com.bsks.api.entity.BsksProduct;
import com.bsks.api.result.Result;

import java.util.List;

public interface RedisService {

    void saveProduct(BsksProduct bsksProduct);

    void saveProducts(List<BsksProduct> bsksProducts);

    void deleteProductById(long productId);

    boolean hasProduct(long productId);

    List<BsksProduct> getProducts();

    BsksProduct findProductById(long productId);

    Result reduceProductQuantity(long productId, int number);

    Result increaseProductQuantity(long productId, int number);

    Result synchroQuantity();
}
