package cn.stylefeng.guns.modular.fin.entity;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;


import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("fin_periodic_order")
public class PeriodicOrder {
    @TableId(value="id", type= IdType.AUTO)
    private Long id;
    private Long memberId;
    @TableField("order_no")
    private String orderNo;
    @TableField("periodic_id")
    private Long periodicId;
    private BigDecimal amount;
    private String symbol;
    private BigDecimal rate ;
    private BigDecimal winN;
    @TableField("is_delete")
    private String isDelete;

    private String result;
    private String remark;
    @TableField("end_time")
    private Date endTime;
    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.UPDATE)
    private Date updateTime;
    /**
     * 创建人
     */
    @TableField(value = "CREATE_USER", fill = FieldFill.INSERT)
    private Long createUser;
    /**
     * 修改人
     */
    @TableField(value = "UPDATE_USER", fill = FieldFill.UPDATE)
    private Long updateUser;
}
