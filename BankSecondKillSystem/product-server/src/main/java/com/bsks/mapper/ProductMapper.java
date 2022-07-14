package com.bsks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bsks.api.entity.BsksProduct;
import com.bsks.entity.ReturnProductRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 抢购存储产品 Mapper 接口
 * </p>
 *
 * @author Li
 * @since 2022-02-22
 */
@Mapper
public interface ProductMapper extends BaseMapper<BsksProduct> {

}
