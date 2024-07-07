package cn.stylefeng.guns.modular.com.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 签到 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2019-12-09 16:28:04
 */

@Data
@TableName("com_check_in")
@Accessors(chain = true)
public class CheckIn extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 签到id
      */
     @TableId(value="check_in_id", type= IdType.AUTO)
     private Long checkInId;

     /**
      * 用户id
      */
     @TableField("member_id")
     private Long memberId;

     /**
      * 币种
      */
     @TableField("coin")
     private String coin;
     /**
      * 连续天数
      */
     @TableField("num")
     private Integer num;
     /**
      * 数量
      */
     @TableField("price")
     private BigDecimal price;
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
