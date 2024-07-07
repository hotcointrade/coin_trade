package cn.stylefeng.guns.modular.e.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 法币交易 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2020-04-03 09:48:13
 */

@Data
@TableName("e_bought")
@Accessors(chain = true)
public class Bought extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 充值
      */
     @TableId(value="bought_id", type= IdType.AUTO)
     private Long boughtId;

     /**
      * 单号
      */
     @TableField("order_no")
     private String orderNo;

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
      * 实际金额
      */
     @TableField("actual_price")
     private java.math.BigDecimal actualPrice;

     /**
      * 购买数量
      */
     @TableField("numbers")
     private java.math.BigDecimal numbers;

     /**
      * 单价
      */
     @TableField("unit")
     private java.math.BigDecimal unit;

     /**
      * 
      */
     @TableField("recharge_address")
     private String rechargeAddress;

     /**
      * 
      */
     @TableField("from_address")
     private String fromAddress;

     /**
      * 唯一标识
      */
     @TableField("tx_hash")
     private String txHash;

     /**
      * 链类型(Omni ERC20 TRC20)
      */
     @TableField("chain_type")
     private String chainType;

     /**
      * 币种
      */
     @TableField("type")
     private String type;

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