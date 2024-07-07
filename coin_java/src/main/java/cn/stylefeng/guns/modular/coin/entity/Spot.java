package cn.stylefeng.guns.modular.coin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 现货交易对 实体类 </p>
 */
@Data
@TableName("coin_spot")
@Accessors(chain = true)
public class Spot extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 现货交易对
      */
     @TableId(value="spot_id", type= IdType.AUTO)
     private Long spotId;

     /**
      * 交易对
      */
     @TableField("symbol")
     private String symbol;

     private String img;

     /**
      * 保留小数位
      */
     @TableField("number")
     private Integer number;

     /**
      * 币种类型（0-平台币  1-火币)
      */
     @TableField("type")
     private String type;

     /**
      * 最小提币量
      */
     @TableField("min_withdraw")
     private java.math.BigDecimal minWithdraw;

     /**
      * 最小区间
      */
     @TableField("min_withdraw_value")
     private BigDecimal minWithdrawValue;

     /**
      * 最大区间
      */
     @TableField("max_withdraw_value")
     private BigDecimal maxWithdrawValue;

     /**
      * 提币手续费
      */
     @TableField("withdraw_fee")
     private java.math.BigDecimal withdrawFee;

     /**
      * 币币交易买入对应币种手续费比例
      */
     @TableField("spot_fee")
     private java.math.BigDecimal spotFee;

     /**
      * 币币交易卖出USDT手续费比例
      */
     @TableField("usdt_spot_fee")
     private BigDecimal usdtSpotFee;


     /**
      * 币币最小委托量
      */
     @TableField("min_buy_number")
     private BigDecimal minBuyNumber;

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

     /**
      * 主编号
      */
     @TableField("code")
     private String code;

     /**
      * 子编号
      */
     @TableField("sub_code")
     private String subCode;


     @TableField("wallet_code")
     private String walletCode;



 }
