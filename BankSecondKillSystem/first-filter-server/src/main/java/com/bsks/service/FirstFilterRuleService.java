package com.bsks.service;

import com.bsks.api.result.Result;
import com.bsks.entity.FirstFilterRule;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * 初筛规则 服务类
 * </p>
 *
 * @author Li
 * @since 2022-02-12
 */
public interface FirstFilterRuleService extends IService<FirstFilterRule> {

    void createFirstFilterRule(FirstFilterRule firstFilterRule);

    Result updateFirstFilterRule(FirstFilterRule firstFilterRule);

    FirstFilterRule getFirstFilterRule();
}
