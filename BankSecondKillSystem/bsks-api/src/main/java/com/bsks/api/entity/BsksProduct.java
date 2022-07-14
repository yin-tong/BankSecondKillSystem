package com.bsks.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 抢购存储产品
 * </p>
 *
 * @author Li
 * @since 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Product 产品对象", description="产品")
public class BsksProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    @ApiModelProperty(value = "产品名称")
    private String name;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "利率")
    private BigDecimal rate;

    @ApiModelProperty(value = "限购数量")
    private Integer limitedQuantity;

    @ApiModelProperty(value = "产品数量")
    private Integer quantity;

    @ApiModelProperty(value = "产品描述")
    private String describe1;

    @ApiModelProperty(value = "商品的秒杀开启时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date killTime;

    @ApiModelProperty(value = "产品对于用户是否可见  0 可见 1 不可见  ")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "乐观锁标识位")
    @Version
    private Long version;

}
