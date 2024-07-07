package cn.stylefeng.guns.modular.fin.entity;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("fin_periodic")
@Accessors(chain = true)
@ToString
public class Periodic {
    @TableId(value="id")
    private Long id;
    //最大金额
    @TableField("num")
    private BigDecimal num;
    //最小金额
    @TableField("min_num")
    private BigDecimal minNum;
    //类型 1周利率 2月利率 3季利率 4年利率
    @TableField("type")
    private String type;
    //名称
    @TableField("name")
    private String name;
    //利率
    @TableField("rate")
    private BigDecimal rate ;
    //备注
    @TableField("remark")
    private String remark;
    //是否删除 Y是N否
    @TableField("is_delete")
    private String isDelete;
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
//    @TableField(value = "CREATE_USER", fill = FieldFill.INSERT)
    @TableField(value = "CREATE_USER")
    private Long createUser;
    /**
     * 修改人
     */
//    @TableField(value = "UPDATE_USER", fill = FieldFill.UPDATE)
    @TableField(value = "UPDATE_USER")
    private Long updateUser;
}
