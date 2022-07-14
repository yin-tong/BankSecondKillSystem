package com.bsks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bsks.api.entity.BsksProduct;
import com.bsks.api.result.Result;
import com.bsks.api.result.ResultCode;
import com.bsks.api.result.ResultException;
import com.bsks.mapper.ProductMapper;
import com.bsks.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bsks.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 抢购存储产品 服务实现类
 * </p>
 *
 * @author Li
 * @since 2022-02-22
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, BsksProduct> implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private RedisService redisService;


    //--------------------------普通用户----------------------------------

    /**
     * 查询产品状态为0的商品信息
     * @return
     */
    @Override
    public List<BsksProduct> findProductByStatusZero(){
        // 从数据库中查数量
        QueryWrapper<BsksProduct> wrapper = new QueryWrapper();
        wrapper.eq ("status",0);
        Integer productNumber = productMapper.selectCount(wrapper);
        if (productNumber == null || productNumber == 0){
            return null;
        }
        // 从redis中查
        List<BsksProduct> products = redisService.getProducts();
        if (products == null || products.size() != productNumber){
            products = productMapper.selectList(wrapper);
            redisService.saveProducts(products);
        }
        return products;
    }

    //-----------------------------管理员-----------------------------------

    /**
     * 获取产品数量
     * @return
     */
    @Override
    public int getProductCount() {
        return productMapper.selectCount(null);
    }

    /**
     *根据时间段查询产品数量
     * @param start
     * @param end
     * @return
     */
    @Override
    public int findByDate(Date start, Date end) {
        QueryWrapper<BsksProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("kill_time",start);
        queryWrapper.le("kill_time",end);
        return productMapper.selectCount(queryWrapper);
    }

    /**
     * 根据时间段查询产品
     * @param start
     * @param end
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public List<BsksProduct> findByDate(Date start, Date end, long currentPage, long pageSize) {
        Page<BsksProduct> page = new Page<>(currentPage,pageSize);
        QueryWrapper<BsksProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("kill_time",start);
        queryWrapper.le("kill_time",end);
        productMapper.selectPage(page,queryWrapper);
        List<BsksProduct> products = page.getRecords();
        if (products != null && products.size()>0){
            for (int i = 0; i < products.size(); i++) {
                if (redisService.hasProduct(products.get(i).getId())){
                    products.set(i,redisService.findProductById(products.get(i).getId()));
                }
            }
        }
        return products;
    }

    /**
     * 根据产品名查询
     * @param productName
     * @return
     */
    @Override
    public int findByName(String productName) {
        QueryWrapper<BsksProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",productName);
        return productMapper.selectCount(queryWrapper);
    }

    @Override
    public List<BsksProduct> findByName(String productName, long currentPage, long pageSize) {
        Page<BsksProduct> page = new Page<>(currentPage,pageSize);
        QueryWrapper<BsksProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",productName);
        productMapper.selectPage(page,queryWrapper);
        List<BsksProduct> products = page.getRecords();
        if (products != null && products.size()>0){
            for (int i = 0; i < products.size(); i++) {
                if (redisService.hasProduct(products.get(i).getId())){
                    products.set(i,redisService.findProductById(products.get(i).getId()));
                }
            }
        }
        return products;
    }

    @Override
    public int findByDateAndName(String productName, Date start, Date end) {
        QueryWrapper<BsksProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",productName);
        queryWrapper.ge("kill_time",start);
        queryWrapper.le("kill_time",end);
        return productMapper.selectCount(queryWrapper);
    }

    @Override
    public List<BsksProduct> findByDateAndName(String productName, Date start, Date end, long currentPage, long pageSize) {
        Page<BsksProduct> page = new Page<>(currentPage,pageSize);
        QueryWrapper<BsksProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",productName);
        queryWrapper.ge("kill_time",start);
        queryWrapper.le("kill_time",end);
        productMapper.selectPage(page,queryWrapper);
        List<BsksProduct> products = page.getRecords();
        if (products != null && products.size()>0){
            for (int i = 0; i < products.size(); i++) {
                if (redisService.hasProduct(products.get(i).getId())){
                    products.set(i,redisService.findProductById(products.get(i).getId()));
                }
            }
        }
        return products;
    }

    /**
     * 获取产品信息
     * @param currentPage 页数
     * @param pageSize 一页的数量
     * @return
     */
    @Override
    public List<BsksProduct> getProduct(long currentPage, long pageSize) {
        Page<BsksProduct> page = new Page<>(currentPage,pageSize);
        productMapper.selectPage(page,null);
        List<BsksProduct> products = page.getRecords();
        if (products != null && products.size()>0){
            for (int i = 0; i < products.size(); i++) {
                if (!redisService.hasProduct(products.get(i).getId()) && products.get(i).getStatus()!=1){
                   redisService.saveProduct(products.get(i));
                }
            }
        }
        return products;
    }


    /**
     * 添加产品信息
     * @param product 产品
     */
    @Override
    public void addProduct(BsksProduct product){
        System.out.println("添加产品："+product);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name",product.getName());
        BsksProduct product1 = productMapper.selectOne(queryWrapper);
        if (product1 != null){
            throw new ResultException(ResultCode.ProductNameRepeat);
        }
        int addProductFlag =  productMapper.insert(product);
        if(addProductFlag == 0){
            throw new ResultException(ResultCode.AddProductERRO);
        }
    }

    /**
     * 根据id删除产品
     * @param ids 多个产品id
     */
    @Override
    public void deleteProduct(List<Long> ids){
        for (long id : ids) {
            int deleteProductFlag = productMapper.deleteById(id);
            if (deleteProductFlag == 0){
                throw new ResultException(ResultCode.DeleteProductERRO);
            }
            if (redisService.hasProduct(id)){
                redisService.deleteProductById(id);
            }
        }
    }

    /**
     * 更新产品信息
     * @param product 产品
     */
    @Transactional
    @Override
    public void updateProduct(BsksProduct product){
        System.out.println("更新产品："+product);
        // 秒杀开始，不能修改产品信息
        BsksProduct bsksProduct = productMapper.selectById(product.getId());
        if (bsksProduct.getKillTime().before(new Date())){
           throw new ResultException(ResultCode.KillOpenNoUpdate);
        }
        int updateProductFlag = productMapper.updateById (product);
        if ( updateProductFlag == 0){
            throw new ResultException(ResultCode.UpdateProductERRO);
        }
        if (redisService.hasProduct(product.getId())){
            redisService.deleteProductById(product.getId());
        }
    }

    /**
     * 根据产品id修改产品状态(0 用户可见， 1用户不可见)
     * @param id
     */
    @Transactional
    @Override
    public void updateProduct(long id, int status){
        if(status != 0 && status != 1){
            throw new ResultException(ResultCode.ParamERRO);
        }
        BsksProduct bsksProduct = productMapper.selectById(id);
        if(bsksProduct == null){
            throw new ResultException(ResultCode.ProductNullError);
        }
        bsksProduct.setStatus(status);
        int updateProductFlag = productMapper.updateById(bsksProduct);
        if (updateProductFlag == 0){
            throw new ResultException(ResultCode.UpdateProductStatusERRO);
        }
        if (redisService.hasProduct(id)){
            redisService.deleteProductById(id);
        }
    }


    //-------------------------------服务-------------------------------

    /**
     * 根据产品id,查询产品信息
     * @param id 产品id
     * @return
     */
    @Override
    public BsksProduct findProductById(long id){
        BsksProduct product = redisService.findProductById(id);
        if (product == null){
            product = productMapper.selectById(id);
            if (product != null){
                redisService.saveProduct(product);
            }
        }
        return product;
    }

    /**
     * 从redis中扣减库存
     * @param productId 产品id
     * @param number 扣减数量
     * @return
     */
    @Override
    public Result reduceProductQuantity(long productId, int number){
        return redisService.reduceProductQuantity(productId, number);
    }

    /**
     * 回退库存：redis中产品加number
     * @param productId 产品id
     * @param number 扣减数量
     * @return
     */
    @Override
    public Result increaseProductQuantity(long productId, int number) {
        return redisService.increaseProductQuantity(productId, number);
    }
}
