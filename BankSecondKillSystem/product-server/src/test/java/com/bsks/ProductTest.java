package com.bsks;

import com.bsks.api.entity.BsksProduct;
import com.bsks.service.ProductService;
import com.bsks.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
public class ProductTest {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ProductService productService;

    @Test
    public void test(){
        BsksProduct bsksProduct = new BsksProduct();
        bsksProduct.setId(1L);
        bsksProduct.setId(1L);
        bsksProduct.setQuantity(4);

        redisService.saveProduct(bsksProduct);

        redisService.reduceProductQuantity(bsksProduct.getId(),2);

        System.out.println(redisService.findProductById(bsksProduct.getId()));
    }
}
