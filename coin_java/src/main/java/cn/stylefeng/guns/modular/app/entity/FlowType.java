package cn.stylefeng.guns.modular.app.entity;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p> 流水类型 实体类 </p>
 */

@Data
@TableName("app_flow_type")
@Accessors(chain = true)
@ToString
public class FlowType  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("type")
    private String type;

    @TableField("code")
    private  String code;

    @TableField("sources")
    private String  sources;

    @TableField("hk_sources")
    private String hkSources;

    @TableField("hg_sources")
    private String hgSources;

    @TableField("jp_sources")
    private String jpSources;

    @TableField("us_sources")
    private String usSource;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;


}