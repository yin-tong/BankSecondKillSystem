package com.bsks.service;

import com.bsks.api.entity.BsksProduct;
import com.bsks.api.result.Result;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 抢购存储产品 服务类
 * </p>
 *
 * @author Li
 * @since 2022-02-22
 */
public interface ProductService extends IService<BsksProduct> {

    List<BsksProduct> findProductByStatusZero();

    void addProduct(BsksProduct product);

    void deleteProduct(List<Long> ids);

    void updateProduct(BsksProduct product);

    BsksProduct findProductById(long id);

    void updateProduct(long id, int status);

    Result increaseProductQuantity(long productId,int number);

    Result reduceProductQuantity(long productId, int number);

    List<BsksProduct> getProduct(long currentPage, long pageSize);

    int getProductCount();

    int findByDate(Date start, Date end);

    List<BsksProduct> findByDate(Date start, Date end, long currentPage, long pageSize);

    int findByName(String productName);

    List<BsksProduct> findByName(String productName, long currentPage, long pageSize);

    int findByDateAndName(String productName, Date start, Date end);

    List<BsksProduct> findByDateAndName(String productName, Date start, Date end, long currentPage, long pageSize);
}
