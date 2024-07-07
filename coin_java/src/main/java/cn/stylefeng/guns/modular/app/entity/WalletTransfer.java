package cn.stylefeng.guns.modular.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 佣金划转 实体类 </p>
  *
  * @author yaying.liu
  * @since 2022-02-18 20:54:40
 */

@Data
@TableName("app_wallet_transfer")
@Accessors(chain = true)
public class WalletTransfer extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 
      */
     @TableId(value="wallet_transfer_id", type= IdType.AUTO)
     private Long walletTransferId;

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
      * 划转数量
      */
     @TableField("price")
     private java.math.BigDecimal price;

     /**
      * 钱包类型
      */
     @TableField("wallet_type")
     private String walletType;

     /**
      * 币种
      */
     @TableField("coin")
     private String coin;

     /**
      * 删除标志
      */
     @TableField("del")
     private String del;




 }