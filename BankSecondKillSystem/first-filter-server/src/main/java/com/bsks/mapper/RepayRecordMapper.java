package com.bsks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 用户的贷款记录 Mapper 接口
 * </p>
 *
 * @author Li
 * @since 2022-02-21
 */
@Mapper
public interface RepayRecordMapper {

    /**
     * 查询身份证为identityId的客户近years年，贷款金额>money,还清天数>payoffDays的逾期次数
     * @param identityId 身份证号码
     * @param years
     * @param money
     * @param payoffDays
     */
    int getLoanTimes(@Param("identityId") String identityId, @Param("years") Integer years,@Param("money")  BigDecimal money,@Param("payoffDays") Integer payoffDays);
}
