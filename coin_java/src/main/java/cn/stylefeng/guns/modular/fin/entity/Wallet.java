package cn.stylefeng.guns.modular.fin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 账户信息 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2020-03-12 17:05:14
 */

@Data
@TableName("fin_wallet")
@Accessors(chain = true)
public class
Wallet extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      *
      */
     @TableId(value="wallet_id", type= IdType.AUTO)
     private Long walletId;

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
      * 锁定额度
      */
     @TableField("locked_price")
     private java.math.BigDecimal lockedPrice;

     /**
      * 质押额度
      */
     @TableField("mortgage_price")
     private java.math.BigDecimal mortgagePrice;

     /**
      * 理财额度
      */
     @TableField("finances_price")
     private java.math.BigDecimal financesPrice;

     /**
      * 锁仓冻结剩余数量
      */
     @TableField("gaslock")
     private java.math.BigDecimal gaslock;

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
