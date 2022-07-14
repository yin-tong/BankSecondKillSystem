package com.bsks.service.impl;

import com.bsks.api.entity.BsksProduct;
import com.bsks.api.result.Result;
import com.bsks.mapper.ProductMapper;
import com.bsks.mapper.ReturnProductRecordMapper;
import com.bsks.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class RedisServiceImpl implements RedisService {

    @Qualifier("myRedisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;

    // 扣减商品库存的lua脚本
    @Qualifier("reduceScript")
    @Autowired
    private DefaultRedisScript<String> reduceScript;

    // 增加商品库存的lua脚本
    @Qualifier("increaseScript")
    @Autowired
    private DefaultRedisScript<String> increaseScript;

    @Autowired
    private ProductMapper productMapper;

    /**
     * 往redis中存储产品
     * @param bsksProduct
     */
    @Override
    public void saveProduct(BsksProduct bsksProduct) {
        redisTemplate.opsForValue().set("product:"+bsksProduct.getId(),bsksProduct);
        redisTemplate.opsForValue().set("productQuantity:"+bsksProduct.getId(),bsksProduct.getQuantity());
        System.out.println("----redis存储商品:"+bsksProduct);
    }

    /**
     * 往redis中存储多个产品
     * @param bsksProducts
     */
    @Override
    public void saveProducts(List<BsksProduct> bsksProducts) {
        for (BsksProduct bsksProduct : bsksProducts) {
            saveProduct(bsksProduct);
        }
    }

    /**
     * 从redis获取所有产品信息
     */
    @Override
    public List<BsksProduct> getProducts(){
        Set<String> keys = redisTemplate.keys("product:*");
        if (keys == null || keys.size() == 0){
            return null;
        }
        List<BsksProduct> products = new ArrayList<>();
        BsksProduct product = null;
        for (String key : keys) {
            product = (BsksProduct) redisTemplate.opsForValue().get(key);
            product.setQuantity((Integer) redisTemplate.opsForValue().get("productQuantity:"+product.getId()));
            products.add(product);
        }
        System.out.println("----redis获取所有商品:"+products);
        return products;
    }

    /**
     * 根据产品id从redis获取商品信息
     * @param productId
     */
    @Override
    public BsksProduct findProductById(long productId){
        BsksProduct bsksProduct = (BsksProduct) redisTemplate.opsForValue().get("product:"+productId);
        if (bsksProduct == null){
            return bsksProduct;
        }
        bsksProduct.setQuantity((Integer) redisTemplate.opsForValue().get("productQuantity:"+productId));
        System.out.println("----redis根据id查询商品:"+bsksProduct);
        return bsksProduct;
    }

    /**
     * 从redis中将账户删除
     * @param productId 产品名称
     */
    @Override
    public void deleteProductById(long productId) {
        redisTemplate.delete("product:"+productId);
        System.out.println("----redis删除产品，产品id:"+productId);
    }

    /**
     * 根据产品id判断是否redis中是否存在产品id
     * @param productId
     * @return
     */
    @Override
    public boolean hasProduct(long productId) {
        return redisTemplate.hasKey("product:"+productId);
    }

    /**
     * 扣减产品库存
     * @param productId 产品id
     */
    @Override
    public Result reduceProductQuantity(long productId,int number){
        String key = "productQuantity:"+productId;
        String value = String.valueOf(number);
        Object is = redisTemplate.execute((RedisConnection connection) -> connection.eval(
                reduceScript.getScriptAsString().getBytes(),
                ReturnType.INTEGER,
                1,
                key.getBytes(StandardCharsets.UTF_8),
                value.getBytes(StandardCharsets.UTF_8))
        );
        if ((Long) is == -2){
            System.out.println("----redis扣减产品库存失败(没有该产品),productId:"+productId+",扣减数量:"+number);
            return new Result(-1,"redis扣减产品库存失败(没有该产品)");
        }else if ((Long) is == -1){
            System.out.println("----redis扣减产品库存失败(库存<购买数量),productId:"+productId+",扣减数量:"+number);
            return new Result("redis扣减产品库存失败(库存<购买数量)");
        }else if ((Long) is == 0){
            System.out.println("----redis扣减产品库存失败(库存为0),productId:"+productId+",扣减数量"+number);
            return new Result(-1,"redis扣减产品库存失败(库存为0)");
        }else {
            System.out.println("----redis扣减产品库存成功,productId:"+productId+",库存-"+number+",库存数量："+is);
            return new Result("redis扣减产品库存成功");
        }
    }

    /**
     * 回退库存: redis中产品数量加number
     * @param productId
     * @param number
     * @return
     */
    @Override
    public Result increaseProductQuantity(long productId, int number) {
        String key = "productQuantity:"+productId;
        String value = String.valueOf(number);
        Object is = redisTemplate.execute((RedisConnection connection) -> connection.eval(
                increaseScript.getScriptAsString().getBytes(),
                ReturnType.INTEGER,
                1,
                key.getBytes(StandardCharsets.UTF_8),
                value.getBytes(StandardCharsets.UTF_8))
        );
        if ((Long) is == 0){
            System.out.println("----redis回退产品库存失败(没有该产品),productId:"+productId+",回退库存数量:"+number);
            return new Result(-1,"redis回退产品库存失败(没有该产品)");
        }else {
            System.out.println("----redis回退产品库存成功,productId:"+productId+",库存+:"+number+",库存数量："+is);
            return new Result("redis回退产品库存成功");
        }
    }

    /**
     * 将redis中的库存同步到数据库
     * @return
     */
    @Override
    public Result synchroQuantity(){
        Set<String> keys = redisTemplate.keys("productQuantity:*");
        if (keys == null || keys.size() == 0){
            return new Result("redis中数据为空，无需同步库存");
        }
        for (String key : keys) {
            int quantity = (int) redisTemplate.opsForValue().get(key);
            Long productId = Long.parseLong(key.substring(16));
            BsksProduct bsksProduct = productMapper.selectById(productId);
            if(bsksProduct.getKillTime().before(new Date())){
                bsksProduct.setQuantity(quantity);
                productMapper.updateById(bsksProduct);
                System.out.println("同步库存成功,产品id: "+productId+",产品库存："+quantity);
            }
        }
        return new Result("同步库存成功");
    }
}
