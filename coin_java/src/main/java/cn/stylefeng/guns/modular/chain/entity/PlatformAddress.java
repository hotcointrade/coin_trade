package cn.stylefeng.guns.modular.chain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 平台地址 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2020-03-16 09:31:12
 */

@Data
@TableName("chain_platform_address")
@Accessors(chain = true)
public class PlatformAddress extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 平台地址
      */
     @TableId(value="platform_address_id", type= IdType.AUTO)
     private Long platformAddressId;

     /**
      * 地址
      */
     @TableField("address")
     private String address;

     /**
      * 类型
      */
     @TableField("type")
     private String type;

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