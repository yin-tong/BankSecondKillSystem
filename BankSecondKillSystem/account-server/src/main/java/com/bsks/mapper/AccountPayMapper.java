package com.bsks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bsks.entity.AccountPay;
import org.apache.ibatis.annotations.*;
/**
 * <p>
 * 支付信息 Mapper 接口
 * </p>
 *
 * @author Li
 * @since 2022-02-24
 */
@Mapper
public interface AccountPayMapper extends BaseMapper<AccountPay> {
}
