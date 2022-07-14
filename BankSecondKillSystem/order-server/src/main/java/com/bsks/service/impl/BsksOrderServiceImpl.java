package com.bsks.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bsks.api.entity.BsksOrder;
import com.bsks.api.result.ResultCode;
import com.bsks.api.result.ResultException;
import com.bsks.mapper.BsksOrderMapper;
import com.bsks.service.BsksOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Li
 * @since 2022-02-22
 */
@Service
public class BsksOrderServiceImpl extends ServiceImpl<BsksOrderMapper, BsksOrder> implements BsksOrderService {

    @Autowired
    private BsksOrderMapper bsksOrderMapper;

    //----------------------普通用户-----------------------------------------

    /**
     * 查询个人订单(状态为0)数量
     * @param accountId 账户id
     * @return
     */
    @Override
    public int selfOrderCount(long accountId){
        QueryWrapper<BsksOrder> wrapper = new QueryWrapper();
        wrapper.eq ("account_id",accountId);
        wrapper.eq ("status",0);
        return bsksOrderMapper.selectCount(wrapper);
    }

    /**
     * 查询个人订单(状态为0)
     * @param accountId 账户id
     * @return
     */
    @Override
    public List<BsksOrder> selfOrder(long accountId, long currentPage,long pageSize){
        Page<BsksOrder> page = new Page<>(currentPage,pageSize);
        QueryWrapper<BsksOrder> wrapper = new QueryWrapper();
        wrapper.eq ("account_id",accountId);
        wrapper.eq ("status",0);
        bsksOrderMapper.selectPage(page,wrapper);
        return page.getRecords();
    }


    /**
     * 删除个人订单(状态变成1)
     * @param orderIds 多个订单id
     * @return Result
     */
    @Override
    public void deleteSelfOrder(List<Long> orderIds){
        for (long orderId : orderIds) {
            BsksOrder bsksOrder = bsksOrderMapper.selectById(orderId);
            if (bsksOrder == null){
                throw new ResultException(ResultCode.DeleteOrderError);
            }
            bsksOrder.setStatus(1);
            bsksOrderMapper.updateById(bsksOrder);
        }
    }

    //----------------------------管理员-----------------------------------------

    /**
     * 获取全部订单数量
     * @return
     */
    @Override
    public int getOrderCount() {
        return bsksOrderMapper.selectCount(null);
    }

    /**
     * 获取全部订单
     * @param currentPage 页数
     * @param pageSize 一页的数据数量
     * @return
     */
    @Override
    public List<BsksOrder> getOrder(long currentPage, long pageSize) {
        Page<BsksOrder> page = new Page<>(currentPage,pageSize);
        bsksOrderMapper.selectPage(page,null);
        return page.getRecords();
    }

    @Override
    public int findByUserNameCount(String userName) {
        QueryWrapper<BsksOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_name",userName);
        return bsksOrderMapper.selectCount(queryWrapper);
    }

    /**
     * 根据客户名字查询
     * @param userName 客户名字
     * @return list
     */
    @Override
    public List<BsksOrder> findByUserName(String userName,long currentPage, long pageSize) {
        Page<BsksOrder> page = new Page<>(currentPage,pageSize);
        QueryWrapper<BsksOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_name",userName);
        bsksOrderMapper.selectPage(page,queryWrapper);
        return page.getRecords();
    }

    @Override
    public int findByPhoneCount(String phone) {
        QueryWrapper<BsksOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("phone",phone);
        return bsksOrderMapper.selectCount(queryWrapper);
    }

    /**
     * 根据手机号码查询
     * @param phone 手机号码
     * @return list
     */
    @Override
    public List<BsksOrder> findByPhone(String phone,long currentPage, long pageSize) {
        Page<BsksOrder> page = new Page<>(currentPage,pageSize);
        QueryWrapper<BsksOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("phone",phone);
        bsksOrderMapper.selectPage(page,queryWrapper);
        return page.getRecords();
    }

    @Override
    public int findByProductNameCount(String productName) {
        QueryWrapper<BsksOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("product_name",productName);
        return bsksOrderMapper.selectCount(queryWrapper);
    }

    /**
     * 根据产品名称查询
     * @param productName 产品名称
     * @return
     */
    @Override
    public List<BsksOrder> findByProductName(String productName,long currentPage, long pageSize) {
        Page<BsksOrder> page = new Page<>(currentPage,pageSize);
        QueryWrapper<BsksOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("product_name",productName);
        bsksOrderMapper.selectPage(page,queryWrapper);
        return page.getRecords();
    }

    /**
     * 根据账户id和产品名称查询
     * @param accountId 账户id
     * @param productName 产品名称
     * @return
     */
    @Override
    public BsksOrder findByAidAndName(long accountId,String productName) {
        QueryWrapper<BsksOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account_id",accountId);
        queryWrapper.eq("product_name",productName);
        return bsksOrderMapper.selectOne(queryWrapper);
    }

    /**
     * 根据id删除订单
     * @param orderId
     */
    @Override
    public void delete(long orderId) {
        int deleteFlag = bsksOrderMapper.deleteById(orderId);
        if (deleteFlag == 0){
            throw new ResultException(ResultCode.DeleteOrderError);
        }
    }

    /**
     *根据多个id删除订单
     * @param orderIds 订单id
     */
    @Override
    public void delete(List<Long> orderIds){
        for (long orderId : orderIds) {
            delete(orderId);
        }
    }

    //---------------------------服务---------------------------------

    /**
     * 根据手机号码,商品名称 查询某账户购买某商品的数量
     * @param phone 手机号码
     * @param productName 商品名称
     * @return
     */
    @Override
    public int findNumbers(String phone, String productName){
        int numbers = 0;
        QueryWrapper<BsksOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone",phone);
        queryWrapper.eq("product_name",productName);
        queryWrapper.eq("type","抢购成功");
        List<BsksOrder> bsksOrders = bsksOrderMapper.selectList(queryWrapper);
        if (bsksOrders == null || bsksOrders.size() == 0){
            return numbers;
        }
        for (BsksOrder bsksOrder : bsksOrders) {
            numbers += bsksOrder.getNumber();
        }
        return numbers;
    }
}
