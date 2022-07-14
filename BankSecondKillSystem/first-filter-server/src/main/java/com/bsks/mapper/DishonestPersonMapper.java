package com.bsks.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 获取失信人信息的 mapper 接口
 * @author Li
 * @since 2022-02-12
 */
@Mapper
public interface DishonestPersonMapper {

    /**
     * 根据身份证号码获取在失信人名单且尚未执行完的记录id
     * 返回不为空，就是失信人
     * @param identityId 身份证号码
     */
    @Select("select id from dishonest_person where identity_id =#{identityId} and execution_status=0")
    Integer getDishonestId(@Param("identityId") String identityId);
}
