package com.bsks.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 产品
 * </p>
 *
 * @author Li
 * @since 2022-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BsksOrder 订单对象", description="订单")
public class BsksOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    @ApiModelProperty(value = "账户id")
    private Long accountId;

    @ApiModelProperty(value = "产品id")
    private Long productId;

    @ApiModelProperty(value = "客户姓名")
    private String userName;

    @ApiModelProperty(value = "客户手机号码")
    private String phone;

    @ApiModelProperty(value = "购买数量")
    private Integer number;

    @ApiModelProperty(value = "产品名称")
    private String productName;

    @ApiModelProperty(value = "限购数量")
    private Integer limitedQuantity;

    @ApiModelProperty(value = "支付金额")
    private BigDecimal payMoney;

    @ApiModelProperty(value = "下单时间")
    private Date orderTime;

    @ApiModelProperty(value = "订单种类: 抢购成功，抢购失败")
    private String type;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "状态(0 客户未删除 1 客户删除)")
    private Integer status;

    @ApiModelProperty(value = "乐观锁标识位")
    @Version
    private Long version;
}
