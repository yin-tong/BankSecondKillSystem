package com.bsks.mapper;


import com.bsks.entity.FirstFilterRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 获取用户信息的 mapper 接口
 * @author Li
 * @since 2022-02-12
 */
@Mapper
public interface ConsumerMapper {

    /**
     * 根据身份证号码获取客户的工作状态
     * @param identityId 身份证号码
     */
    @Select("select work_status from customer_data where identity_id=#{identityId} ")
    String getWorkStatus(@Param("identityId") String identityId);
}
