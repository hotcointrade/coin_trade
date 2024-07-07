package cn.stylefeng.guns.modular.e.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 锁仓合约配置 实体类 </p>
  *
  * @author yaying.liu
  * @since 2020-09-25 10:44:07
 */

@Data
@TableName("e_lock")
@Accessors(chain = true)
public class Lock extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 锁仓合约配置
      */
     @TableId(value="lock_id", type= IdType.AUTO)
     private Long lockId;

     /**
      * 锁仓周期
      */
     @TableField("name")
     private String name;

     /**
      * 合约天数
      */
     @TableField("days")
     private Long days;

     /**
      * 每日静态收益率（%）
      */
     @TableField("rate")
     private String rate;

     /**
      * 版本号
      */
     @TableField("version")
     @Version
     private Long version;

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