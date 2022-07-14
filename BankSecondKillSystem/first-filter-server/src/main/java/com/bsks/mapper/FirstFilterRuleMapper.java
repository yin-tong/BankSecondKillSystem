package com.bsks.mapper;

import com.bsks.entity.FirstFilterRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 初筛的拒绝规则 Mapper 接口
 * </p>
 *
 * @author Li
 * @since 2022-02-12
 */
@Mapper
public interface FirstFilterRuleMapper extends BaseMapper<FirstFilterRule> {

    /**
     * 创建默认初筛规则，防止管理员忘记设置规则
     */
    @Insert("insert into first_filter_rule(id,limit_overdue_years,limit_overdue_times,limit_overdue_money,limit_payoff_days,limit_age) values(1,3,2,1000,3,18)")
    void createDefaultFirstFilterRule();

    /**
     * 获得初筛规则(只能有一个初筛规则)
     */
    @Select("select * from first_filter_rule limit 1 ")
    FirstFilterRule getFirstFilterRule();


}
