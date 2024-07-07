package cn.stylefeng.guns.modular.e.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 生活支付开通记录 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2020-06-28 16:17:00
 */

@Data
@TableName("e_live")
@Accessors(chain = true)
public class Live extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 生活支付开通记录
      */
     @TableId(value="live_id", type= IdType.AUTO)
     private Long liveId;

     /**
      * 用户
      */
     @TableField("member_id")
     private Long memberId;

     /**
      * 金额
      */
     @TableField("price")
     private java.math.BigDecimal price;

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