package com.bsks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bsks.entity.FirstFilterRecord;
import com.bsks.entity.FirstFilterRule;
import com.bsks.mapper.*;
import com.bsks.service.FirstFilterRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 初筛记录表 服务实现类
 * </p>
 *
 * @author Li
 * @since 2022-02-22
 */
@Service
public class FirstFilterRecordServiceImpl extends ServiceImpl<FirstFilterRecordMapper, FirstFilterRecord> implements FirstFilterRecordService {

    @Autowired
    private FirstFilterRecordMapper firstFilterRecordMapper;

    @Autowired
    private ConsumerMapper consumerMapper;

    @Autowired
    private DishonestPersonMapper dishonestPersonMapper;

    @Autowired
    private FirstFilterRuleMapper firstFilterRuleMapper;

    @Autowired
    private RepayRecordMapper loanRecordMapper;



    /**
     * 创建拦截记录
     * @param identityId 身份证号码
     * @param result 初筛结果：通过或拒绝
     */
    @Override
    public void createFirstFilterRecord(String identityId, String result, String rejectReason){
        //1. 查询记录是否存在
        QueryWrapper<FirstFilterRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("identity_id",identityId);
        FirstFilterRecord firstFilterRecord = firstFilterRecordMapper.selectOne(queryWrapper);
        // 数据库中已经有记录，更新记录
        if (firstFilterRecord != null){
            firstFilterRecord.setResult(result);
            firstFilterRecord.setRejectReason(rejectReason);
            firstFilterRecordMapper.updateById(firstFilterRecord);
        } else { // 数据库中没有记录，创建记录
            firstFilterRecord = new FirstFilterRecord();
            firstFilterRecord.setIdentityId(identityId);
            firstFilterRecord.setResult(result);
            firstFilterRecord.setRejectReason(rejectReason);
            firstFilterRecordMapper.insert(firstFilterRecord);
        }
    }

    /**
     * 根据身份证号码判断用户是否通过初筛规则
     * @param identityId 身份证号码
     */
    @Override
    public boolean isRejected(String identityId,int age) {
        //1.判断数据库中初筛规则是否为空，如果为空，创建默认初筛规则
        FirstFilterRule firstFilterRule = firstFilterRuleMapper.getFirstFilterRule();
        if (firstFilterRule == null){
            firstFilterRuleMapper.createDefaultFirstFilterRule();
        }
        //2.用户通过初筛的四个规则，未通过返回false
        Integer limitOverdueYears = firstFilterRule.getLimitOverdueYears();
        Integer limitOverdueTimes = firstFilterRule.getLimitOverdueTimes();
        BigDecimal limitOverdueMoney = firstFilterRule.getLimitOverdueMoney();
        Integer limitPayoffDays = firstFilterRule.getLimitPayoffDays();
        //3.客户逾期记录不合格，返回false
        int loanTimes = loanRecordMapper.getLoanTimes(identityId,limitOverdueYears,limitOverdueMoney,limitPayoffDays);
        if ( loanTimes>limitOverdueTimes){
            createFirstFilterRecord(identityId,"拒绝","逾期记录不合格");
            System.out.println("---拒绝原因，逾期记录不合格,身份证号码："+identityId);
            return false;
        }
        //4. 客户工作状态为无业返回false
        if ("失业".equals(consumerMapper.getWorkStatus(identityId))){
            createFirstFilterRecord(identityId,"拒绝","客户工作状态为失业");
            System.out.println("---拒绝原因：客户工作状态为失业,身份证号码："+identityId);
            return false;
        }
        //5. 客户在失信人名单，返回false
        if (dishonestPersonMapper.getDishonestId(identityId) != null){
            createFirstFilterRecord(identityId,"拒绝","客户在失信人名单中且尚未执行完");
            System.out.println(3);
            System.out.println("---拒绝原因：客户在失信人名单中且尚未执行完,身份证号码："+identityId);
            return false;
        }
        //6. 如果客户年龄小于限制年龄，返回false
        if ( age <firstFilterRule.getLimitAge()){
            createFirstFilterRecord(identityId,"拒绝","年龄不足");
            System.out.println("---拒绝原因：年龄不足,身份证号码："+identityId);
            return false;
        }
        // 通过初筛
        createFirstFilterRecord(identityId,"通过","");
        return true;
    }

    /**
     * 获取所有记录数量
     */
    @Override
    public int findAllRecordCount(){
        return firstFilterRecordMapper.selectCount(null);
    }

    /**
     * 获取所有记录
     * @param currentPage 页号
     * @param pageSize 页面数据数量
     */
    @Override
    public List<FirstFilterRecord> findAllRecord(long currentPage, long pageSize){
        Page<FirstFilterRecord> page = new Page<>(currentPage,pageSize);
        firstFilterRecordMapper.selectPage(page,null);
        return page.getRecords();
    }

    /**
     * 获取时间段内所有记录数量
     * @param start 开始时间
     * @param end 结束时间
     */
    @Override
    public int findRecordCountByDate(Date start,Date end){
        QueryWrapper<FirstFilterRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("update_time",start);
        queryWrapper.le("update_time",end);
        return firstFilterRecordMapper.selectCount(queryWrapper);
    }

    /**
     * 获取时间内所有记录
     * @param start 开始时间
     * @param end 结束时间
     * @param currentPage 页号
     * @param pageSize 一页数据的数量
     */
    @Override
    public List<FirstFilterRecord> findRecordByDate(Date start,Date end,long currentPage, long pageSize){
        Page<FirstFilterRecord> page = new Page<>(currentPage,pageSize);
        QueryWrapper<FirstFilterRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("update_time",start);
        queryWrapper.le("update_time",end);
        firstFilterRecordMapper.selectPage(page,queryWrapper);
        return page.getRecords();
    }

    /**
     * 获取身份证号相同的所有记录数量
     * @param identityId 身份证号码
     */
    @Override
    public int findRecordCountByIdentityId(String identityId){
        QueryWrapper<FirstFilterRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("identity_id",identityId);
        return firstFilterRecordMapper.selectCount(queryWrapper);
    }

    /**
     * 获取身份证号相同的所有记录
     * @param identityId 身份证号码
     * @param currentPage 页号
     * @param pageSize 页面数据数量
     */
    @Override
    public List<FirstFilterRecord> findRecordByIdentityId(String identityId,long currentPage, long pageSize){
        Page<FirstFilterRecord> page = new Page<>(currentPage,pageSize);
        QueryWrapper<FirstFilterRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("identity_id",identityId);
        firstFilterRecordMapper.selectPage(page,queryWrapper);
        return page.getRecords();
    }

    /**
     * 根据身份证号，时间段获取记录数量
     * @param identityId 身份证号码
     * @param start 开始时间
     * @param end 结束时间
     */
    @Override
    public int findRecordCountByIdentityIdDate(String identityId,Date start,Date end){
        QueryWrapper<FirstFilterRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("identity_id",identityId);
        queryWrapper.ge("update_time",start);
        queryWrapper.le("update_time",end);
        return firstFilterRecordMapper.selectCount(queryWrapper);
    }

    /**
     * 根据身份证号，时间段获取记录
     * @param identityId 身份证号码
     * @param start 开始时间
     * @param end 结束时间
     */
    @Override
    public List<FirstFilterRecord> findRecordByIdentityIdDate(String identityId, Date start, Date end, long currentPage, long pageSize){
        Page<FirstFilterRecord> page = new Page<>(currentPage,pageSize);
        QueryWrapper<FirstFilterRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("identity_id",identityId);
        queryWrapper.ge("update_time",start);
        queryWrapper.le("update_time",end);
        firstFilterRecordMapper.selectPage(page,queryWrapper);
        return page.getRecords();
    }
}
