package cn.stylefeng.guns.modular.bulletin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;


/**
 * <p>
 * 客服表
 * </p>
 */
@TableName("pro_contact")
@Data
@ToString
public class Contact extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId(value ="contact_id",type = IdType.ID_WORKER)
    private Long contactId;
    /**
     * 联系人
     */
    @TableField("contact_name")
    private String contactName;
    /**
     * 联系电话
     */
    @TableField("phone")
    private String phone;
    /**
     * 状态
     */
    @TableField("status")
    private String status;
    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    @TableField("start")
    private Date start;

    @TableField("end")
    private Date end;



}
