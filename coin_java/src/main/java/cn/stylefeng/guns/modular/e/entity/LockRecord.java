package cn.stylefeng.guns.modular.e.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 锁仓记录 实体类 </p>
  *
  * @author yaying.liu
  * @since 2020-09-25 10:46:59
 */

@Data
@TableName("e_lock_record")
@Accessors(chain = true)
public class LockRecord extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 锁仓记录
      */
     @TableId(value="lock_record_id", type= IdType.AUTO)
     private Long lockRecordId;

     /**
      * 用户
      */
     @TableField("member_id")
     private Long memberId;

     /**
      * 锁仓id
      */
     @TableField("lock_id")
     private Long lockId;

     /**
      * 锁仓周期
      */
     @TableField("lock_name")
     private String lockName;

     /**
      * 锁仓天数
      */
     @TableField("days")
     private Long days;

     /**
      * 每日静态收益率（%）
      */
     @TableField("rate")
     private String rate;

     /**
      * 锁仓数量（MGE）
      */
     @TableField("number")
     private java.math.BigDecimal number;

     /**
      * 锁仓价值(USDT)
      */
     @TableField("price")
     private java.math.BigDecimal price;

     /**
      * 锁仓起始时间
      */
     @TableField("start_time")
     private java.util.Date startTime;

     /**
      * 解锁时间
      */
     @TableField("end_time")
     private java.util.Date endTime;

     /**
      * 收益次数
      */
     @TableField("profit_count")
     private Long profitCount;

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