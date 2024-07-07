package cn.stylefeng.guns.modular.meta_data.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统参数表
 * </p>
 */
@TableName("sys_config")
@Data
@ToString
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId(value = "config_id", type = IdType.ID_WORKER)
    private Long configId;
    /**
     * 属性编码
     */
    @TableField("code")
    private String code;
    /**
     * 属性值
     */
    @TableField("value")
    private String value;
    /**
     * 属性名称
     */
    @TableField("name")
    private String name;
    /**
     * 状态   Y：启用   N：禁用
     */
    @TableField("status")
    private String status;

     /**
     * 参数类型 （SYSTEM-系统参数 COMMON-公用）
     */
    @TableField("type")
    private String type;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 删除 Y：是 N:否
     */
    @TableField("del_flag")
    private String delFlag;
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
