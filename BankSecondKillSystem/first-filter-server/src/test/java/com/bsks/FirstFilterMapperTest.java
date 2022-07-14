package com.bsks;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bsks.entity.FirstFilterRule;
import com.bsks.mapper.FirstFilterRuleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
public class FirstFilterMapperTest {

    @Autowired
    private FirstFilterRuleMapper firstFilterRuleMapper;

    /**
     * 测试查询
     */
    @Test
    public void testSearch() {
        System.out.println(firstFilterRuleMapper.selectById(1));
    }

    /**
     * 更新操作
     */
    @Test
    public void test1() {
        //1. 查出对象
        FirstFilterRule firstFilterRule = firstFilterRuleMapper.getFirstFilterRule();
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id",firstFilterRule.getId());
        firstFilterRule.setLimitAge(18);
        firstFilterRuleMapper.update(firstFilterRule,updateWrapper);
    }

    /**
     * 分页查询
     */
    @Test
    public void test2() {
        Page<FirstFilterRule> firstFilterRulePage = new Page(1,5);
        firstFilterRuleMapper.selectPage(firstFilterRulePage,null);
    }
}
