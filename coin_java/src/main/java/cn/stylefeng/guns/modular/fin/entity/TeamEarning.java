package cn.stylefeng.guns.modular.fin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 团队佣金 实体类 </p>
  *
  * @author yaying.liu
  * @since 2022-02-18 18:53:54
 */

@Data
@TableName("fin_invite_wallet")
@Accessors(chain = true)
public class TeamEarning extends BaseEntity implements Serializable {

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
      * 账户
      */
     @TableField("account")
     private String account;

     /**
      * 可用额度
      */
     @TableField("used_price")
     private java.math.BigDecimal usedPrice;

     @TableField("type")
     private String type;

     /**
      * 已提现
      */
     @TableField("withdraw_price")
     private java.math.BigDecimal withdrawPrice;

     /**
      * 封禁额度
      */
     @TableField("blocked_price")
     private java.math.BigDecimal blockedPrice;

     /**
      * 删除标志
      */
     @TableField("del")
     private String del;




 }
