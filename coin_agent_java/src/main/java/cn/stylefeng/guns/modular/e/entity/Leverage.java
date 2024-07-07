package cn.stylefeng.guns.modular.e.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 杠杆倍率 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2020-03-23 09:19:33
 */

@Data
@TableName("e_leverage")
@Accessors(chain = true)
public class Leverage extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 
      */
     @TableId(value="leverage_id", type= IdType.AUTO)
     private Long leverageId;

     /**
      * 名称
      */
     @TableField("name")
     private String name;

     /**
      * 倍率
      */
     @TableField("value")
     private java.math.BigDecimal value;

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