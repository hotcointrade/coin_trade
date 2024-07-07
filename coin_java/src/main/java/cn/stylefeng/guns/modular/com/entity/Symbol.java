package cn.stylefeng.guns.modular.com.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 币种 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2020-03-09 11:03:53
 */

@Data
@TableName("com_symbol")
@Accessors(chain = true)
public class Symbol extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 
      */
     @TableId(value="symbol_id", type= IdType.AUTO)
     private Long symbolId;

     /**
      * 名称
      */
     @TableField("name")
     private String name;

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