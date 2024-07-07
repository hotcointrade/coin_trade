package cn.stylefeng.guns.modular.bulletin.entity;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 短信表
 */
@TableName("com_sms")
@Data
@Accessors(chain =true)
public class Sms extends BaseEntity {

    @TableId(value = "sms_id",type = IdType.AUTO)
    private Long smsId;

    @TableField("member_id")
    private Long memberId;
    @TableField("content")
    private String content;
    @TableField("type")
    private String type;
    @TableField("status")
    private String status;
    @TableField("del")
    private String del;
    @TableField("remark")
    private String remark;


}