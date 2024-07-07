package cn.stylefeng.guns.modular.fin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 钱包历史变更 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2019-12-09 17:06:57
 */

@Data
@TableName("fin_wallet_history")
@Accessors(chain = true)
public class WalletHistory extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 
      */
     @TableId(value="wallet_history_id", type= IdType.AUTO)
     private Long walletHistoryId;

     /**
      * 
      */
     @TableField("member_id")
     private Long memberId;

     /**
      * 钱包总额
      */
     @TableField("total_price")
     private java.math.BigDecimal totalPrice;

     /**
      * 平台返还总额
      */
     @TableField("return_price")
     private java.math.BigDecimal returnPrice;

     /**
      * 消费券总额
      */
     @TableField("ticket_price")
     private java.math.BigDecimal ticketPrice;

     /**
      * 类型：BTC 、CNY:人民币、 POINT：积分
      */
     @TableField("type")
     private String type;

     /**
      * 版本
      */
     @TableField("version")
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