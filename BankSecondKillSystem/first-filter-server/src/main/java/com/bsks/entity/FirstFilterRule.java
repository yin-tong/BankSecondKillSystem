package com.bsks.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 初筛的拒绝规则
 * </p>
 *
 * @author Li
 * @since 2022-02-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="FirstFilterRule 初筛的拒绝规则", description="初筛的拒绝规则")
public class FirstFilterRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "规则id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    @ApiModelProperty(value = "逾期年数限制")
    private Integer limitOverdueYears;

    @ApiModelProperty(value = "逾期次数限制")
    private Integer limitOverdueTimes;

    @ApiModelProperty(value = "逾期金额限制")
    private BigDecimal limitOverdueMoney;

    @ApiModelProperty(value = "债务还清的天数")
    private Integer limitPayoffDays;

    @ApiModelProperty(value = "客户年龄限制")
    private Integer limitAge;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "乐观锁标识位")
    @Version
    private Long version;


}
