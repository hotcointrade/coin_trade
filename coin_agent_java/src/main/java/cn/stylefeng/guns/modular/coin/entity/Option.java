package cn.stylefeng.guns.modular.coin.entity;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p> 期权交易对 实体类 </p>
 */
@Data
@TableName("coin_option")
@Accessors(chain = true)
public class Option extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      * 期权交易对
      */
     @TableId(value="spot_id", type= IdType.AUTO)
     private Long spotId;

     /**
      * 期权对
      */
     @TableField("symbol")
     private String symbol;
     /**
      * 保留小数位
      */
     @TableField("sym_name")
     private String symName;

     /**
      * 币种 LTC
      */
     @TableField("type")
     private String type;
     /**
      * 基础币种 USDT
      */
     @TableField("base_type")
     private String baseType;
     /**
      *排序
      */
     @TableField("sort")
     private Integer sort;

     /**
      * 交易币小数精度
      */
     @TableField("symbol_jd")
     private Integer symbolJd;

     /**
      * 基币小数精度
      */
     @TableField("base_jd")
     private Integer baseJd;

     /**
      * 启用/禁止 0/1
      */
     @TableField("startt")
     private String startt;

     /**
      * 显示 是/否 1/0
      */
     @TableField("showw")
     private String showw;

     /**
      * 平局处理 退回资金/平台通吃 1/0
      */
     @TableField("processing")
     private String processing;

     /**
      * 允许看涨 允许/不允许 1/0
      */
     @TableField("allow_show_rise")
     private String allowShowRise;

     /**
      * 允许看跌 允许/不允许 1/0
      */
     @TableField("allow_show_fall")
     private String allowShowFall;

     /**
      * 允许投注金额 10,20,50,100,200,500,1000
      */
     @TableField("allow_bet_price")
     private String allowBetPrice;
     /**
      * 开仓手续费
      */
     @TableField("opening_charge")
     private BigDecimal openingCharge;

     /**
      * 赢家抽水费
      */
     @TableField("pumping_fee")
     private BigDecimal pumpingFee;

     /**
      * 初始买涨奖池金额
      */
     @TableField("buy_up_bonus_amount")
     private BigDecimal buyUpBonusAmount;

     /**
      * 初始买跌奖池金额
      */
     @TableField("buy_down_bonus_amount")
     private BigDecimal buyDownBonusAmount;

     /**
      * 投注时间周期(秒)
      */
     @TableField("betting_time_cycle")
     private Integer bettingTimeCycle;

     /**
      * 开奖时间周期(秒)
      */
     @TableField("drawing_time_cycle")
     private Integer drawingTimeCycle;


}