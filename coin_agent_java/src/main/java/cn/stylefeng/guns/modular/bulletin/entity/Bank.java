package cn.stylefeng.guns.modular.bulletin.entity;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 银行表
 */
@TableName("com_bank")
@Data
@Accessors(chain =true)
public class Bank extends BaseEntity {

    @TableId(value = "bank_id",type = IdType.AUTO)
    private Long bankId;

    @TableField("code")
    private String code;

    @TableField("name")
    private String name;

    @TableField("short_name")
    private String shortName;

    @TableField("status")
    private String status;

    @TableField("del")
    private String del;

    @TableField("remark")
    private String remark;
}