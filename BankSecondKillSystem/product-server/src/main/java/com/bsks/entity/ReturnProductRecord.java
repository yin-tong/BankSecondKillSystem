package com.bsks.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ReturnProductRecord 对象", description="回退库存记录")
public class ReturnProductRecord {

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "产品id")
    private Long productId;

    @ApiModelProperty(value = "回退库存数量")
    private Integer number;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
