package cn.stylefeng.guns.modular.e.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 锁仓静态收益记录 实体类 </p>
  *
  * @author yaying.liu
  * @since 2020-09-25 10:46:24
 */

@Data
@TableName("e_lock_profit")
@Accessors(chain = true)
public class LockProfit extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 锁仓静态收益记录
      */
     @TableId(value="lock_profit_id", type= IdType.AUTO)
     private Long lockProfitId;

     /**
      * 用户
      */
     @TableField("member_id")
     private Long memberId;

     /**
      * 锁仓记录id
      */
     @TableField("lock_record_id")
     private Long lockRecordId;

     /**
      * 币种
      */
     @TableField("coin")
     private String coin;

     /**
      * 收益数量
      */
     @TableField("number")
     private java.math.BigDecimal number;

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