package cn.stylefeng.guns.modular.fin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 平台流水 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2020-01-03 18:06:11
 */

@Data
@TableName("fin_platform_cashflow")
@Accessors(chain = true)
public class PlatformCashflow extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 流水id
      */
     @TableId(value="platform_cashflow_id", type= IdType.AUTO)
     private Long platformCashflowId;

     /**
      * 用户id
      */
     @TableField("member_id")
     private Long memberId;

     /**
      * 账户类型（BTC、  T:算力、POINT:积分）
      */
     @TableField("wallet_type")
     private String walletType;

     /**
      * 流水方向（1：流入(+)      0 :流出(-) ）
      */
     @TableField("flow_op")
     private Long flowOp;

     /**
      * 流水类型
      */
     @TableField("flow_type")
     private String flowType;

     /**
      * 流水记录编码(备用）
      */
     @TableField("item_code")
     private String itemCode;

     /**
      * 名称（备用）
      */
     @TableField("item_name")
     private String itemName;

     /**
      * 变更前额度
      */
     @TableField("before_price")
     private java.math.BigDecimal beforePrice;

     /**
      * 变更后额度
      */
     @TableField("after_price")
     private java.math.BigDecimal afterPrice;

     /**
      * 流水金额
      */
     @TableField("flow_price")
     private java.math.BigDecimal flowPrice;

     /**
      * 流水币种（BTC、  T:算力、POINT:积分）
      */
     @TableField("flow_coin")
     private String flowCoin;

     /**
      * 实际金额
      */
     @TableField("actual_price")
     private java.math.BigDecimal actualPrice;

     /**
      * 实际币种BTC、T:算力、POINT:积分）
      */
     @TableField("actual_coin")
     private String actualCoin;

     /**
      * 手续费
      */
     @TableField("fee")
     private java.math.BigDecimal fee;

     /**
      * 手续费币种
      */
     @TableField("fee_coin")
     private String feeCoin;

     /**
      * 流水来源
      */
     @TableField("source")
     private String source;

     /**
      * 来处
      */
     @TableField("from_id")
     private Long fromId;

     /**
      * 去向
      */
     @TableField("to_id")
     private Long toId;

     /**
      * 明细（备用）
      */
     @TableField("detail")
     private String detail;

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