package cn.stylefeng.guns.modular.fin.entity;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
  * <p> 期权账户 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2020-03-12 19:10:50
 */

@Data
@TableName("fin_option")
@Accessors(chain = true)
public class FinOption extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 币期权账户
      */
     @TableId(value="currency_id", type= IdType.AUTO)
     private Long currencyId;

     /**
      * 用户id
      */
     @TableField("member_id")
     private Long memberId;

     /**
      * 可用额度
      */
     @TableField("used_price")
     private java.math.BigDecimal usedPrice;

     /**
      * 冻结额度
      */
     @TableField("locked_price")
     private java.math.BigDecimal lockedPrice;

     /**
      * 类型
      */
     @TableField("type")
     private String type;

     /**
      * 版本
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
      * 钱包大类型
      */
     @TableField("remark")
     private String remark;




 }