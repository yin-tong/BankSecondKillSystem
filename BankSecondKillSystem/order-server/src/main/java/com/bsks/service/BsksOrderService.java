package com.bsks.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bsks.api.entity.BsksOrder;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Li
 * @since 2022-02-22
 */
public interface BsksOrderService extends IService<BsksOrder> {

    int selfOrderCount(long accountId);

    List<BsksOrder> selfOrder(long accountId, long currentPage, long pageSize);

    void deleteSelfOrder(List<Long> orderIds);

    int getOrderCount();

    List<BsksOrder> getOrder(long currentPage, long pageSize);

    int findByUserNameCount(String userName);

    List<BsksOrder> findByUserName(String userName,long currentPage, long pageSize);

    int findByPhoneCount(String phone);

    List<BsksOrder> findByPhone(String phone,long currentPage, long pageSize);

    int findByProductNameCount(String productName);

    List<BsksOrder> findByProductName(String productName,long currentPage, long pageSize);

    void delete(List<Long> orderIds);

    void delete(long orderId);

    BsksOrder findByAidAndName(long accountId,String productName);

    int findNumbers(String phone, String productName);
}
