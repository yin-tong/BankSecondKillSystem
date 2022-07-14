package com.bsks;
import com.bsks.api.entity.BsksProduct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;

@SpringBootTest
public class RedisTests {

    @Qualifier("myRedisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;

    @Qualifier("reduceScript")
    @Autowired
    private DefaultRedisScript<String> reduceScript;

    public boolean reduceProduct(long productId,long number) {
        String key = "productNumber:"+productId;
        String value = String.valueOf(number);
        Object is = redisTemplate.execute((RedisConnection connection) -> connection.eval(
                reduceScript.getScriptAsString().getBytes(),
                ReturnType.BOOLEAN,
                1,
                key.getBytes(StandardCharsets.UTF_8),
                value.getBytes(StandardCharsets.UTF_8))
        );
        return (Boolean) is;
    }

    /**
     * redis存商品
     */
    @Test
    public void addProduct() {
        BsksProduct product = new BsksProduct();
        product.setId(1L);
        product.setQuantity(12);
        redisTemplate.opsForValue().set("product:"+product.getId(),product);
    }

    /**
     * redis减库存
     */
    @Test
    public void testReduceProduct() {
        long productId = 1;
        long stock = 3;
        redisTemplate.opsForValue().set("productNumber:"+productId,stock);
        long number = 2;
        for (int i = 0; i < 2; i++) {
            boolean b = reduceProduct(productId, 2);
            if (b){
                System.out.println(i +"购买成功");
            }else {
                System.out.println(i +"购买失败");
            }
        }
        System.out.println(redisTemplate.opsForValue().get("productNumber:"+productId));

    }



}
