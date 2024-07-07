package cn.stylefeng.guns.modular.bulletin.entity;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * app版本信息
 */
@Data
@Accessors(chain = true)
public class AppVersion extends BaseEntity {

    @TableId(value = "app_version_id",type = IdType.AUTO)
    private Long appVersionId;

    @TableField("version")
    private String version;
    @TableField("content")
    private String content;
    @TableField("address")
    private String address;
    @TableField("type")
    private String type;

    @TableField("status")
    private String status;
    @TableField("del")
    private String del;
    @TableField("remark")
    private String remark;


}