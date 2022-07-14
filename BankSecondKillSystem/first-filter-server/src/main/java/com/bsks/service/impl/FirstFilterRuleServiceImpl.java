package com.bsks.service.impl;

import com.bsks.api.result.Result;
import com.bsks.api.result.ResultCode;
import com.bsks.api.result.ResultException;
import com.bsks.entity.FirstFilterRule;
import com.bsks.mapper.*;
import com.bsks.service.FirstFilterRecordService;
import com.bsks.service.FirstFilterRuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bsks.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 初筛的拒绝规则 服务实现类
 * </p>
 *
 * @author Li
 * @since 2022-02-12
 */
@Service
public class FirstFilterRuleServiceImpl extends ServiceImpl<FirstFilterRuleMapper, FirstFilterRule> implements FirstFilterRuleService {

    @Autowired
    private FirstFilterRuleMapper firstFilterRuleMapper;

    @Autowired
    private RedisService redisService;


    /**
     * 创建规则
     * @param firstFilterRule
     */
    @Override
    public void createFirstFilterRule(FirstFilterRule firstFilterRule) {
        firstFilterRuleMapper.insert(firstFilterRule);
    }

    /**
     * 更新规则
     * @param firstFilterRule
     */
    @Override
    public Result updateFirstFilterRule(FirstFilterRule firstFilterRule) {
        int updateFlag = firstFilterRuleMapper.updateById(firstFilterRule);
        if (updateFlag == 0){
            return new Result(ResultCode.RuleUpdateError);
        }
        if (redisService.hasFirstFilter()){
            redisService.saveUpdateFirstFilterRule(firstFilterRule);
        }
        return new Result("更新成功");
    }

    /**
     * 获取初筛规则
     */
    @Override
    public FirstFilterRule getFirstFilterRule() {
        // 从redis获取初筛规则
        FirstFilterRule firstFilterRule = redisService.getFirstFilterRule();
        // redis为空，从mysql中查询
        if (firstFilterRule == null){
            firstFilterRule = firstFilterRuleMapper.getFirstFilterRule();
            // mysql中没有，创建默认规则
            if (firstFilterRule == null){
                firstFilterRuleMapper.createDefaultFirstFilterRule();
            }
            // 存到redis中
            redisService.saveUpdateFirstFilterRule(firstFilterRule);
        }
        return firstFilterRule;
    }
}
