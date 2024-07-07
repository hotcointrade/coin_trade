package cn.stylefeng.guns.modular.promotion.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 网关定义 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2019-10-11 11:20:42
 */

@Data
@TableName("gateway_define")
@Accessors(chain = true)
public class GatewayDefine extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 网关定义id
      */
     @TableId(value="gateway_define_id", type= IdType.AUTO)
     private Long gatewayDefineId;

     /**
      * 网关编码
      */
     @TableField("code")
     private String code;

     /**
      * 网关名称
      */
     @TableField("name")
     private String name;

     /**
      * 网关值
      */
     @TableField("value")
     private String value;

     /**
      * 状态(字典)
      */
     @TableField("status")
     private String status;

     /**
      * 删除标志
      */
     @TableField("del")
     private String del;

     /**
      * 备注
      */
     @TableField("remark")
     private String remark;




 }