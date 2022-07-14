package com.bsks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bsks.api.entity.Account;
import org.apache.ibatis.annotations.*;


/**
 * <p>
 * 账户信息表 Mapper 接口
 * </p>
 *
 * @author Li
 * @since 2022-02-10
 */
@Mapper
public interface AccountMapper extends BaseMapper<Account> {
}
