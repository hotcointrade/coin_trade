package cn.stylefeng.guns.modular.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;

/**
 * 通用基类实体
 *  -- 包含字段 WHO 操作记录信息
 */
@Data
@ToString
public class BaseEntity {

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
