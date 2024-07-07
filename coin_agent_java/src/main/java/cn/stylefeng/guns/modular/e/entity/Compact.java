package cn.stylefeng.guns.modular.e.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import lombok.experimental.Accessors;

/**
  * <p> 合约订单 实体类 </p>
  *
  * @author yaying.liu
  * @Date 2020-03-23 14:35:24
 */

@Data
@TableName("e_compact")
@Accessors(chain = true)
public class Compact extends BaseEntity implements Serializable {

     private static final long serialVersionUID = 1L;

     /**
      *
      */
     @TableId(value="compact_id", type= IdType.AUTO)
     private Long compactId;

     /**
      * 订单号
      */
     @TableField("order_no")
     private String orderNo;

     /**
      * 用户
      */
     @TableField("member_id")
     private Long memberId;

     /**
      * 交易方式
      */
     @TableField("deal_way")
     private String dealWay;

     /**
      * 交易类型(买入开多、卖出开多)
      */
     @TableField("compact_type")
     private String compactType;

     /**
      * 交易对
      */
     @TableField("symbols")
     private String symbols;

     /**
      * 单价
      */
     @TableField("unit")
     private java.math.BigDecimal unit;

     /**
      * 每手的价值
      */
     @TableField("hand_number")
     private Double handNumber;

     /**
      * 持仓手数
      */
     @TableField("numbers")
     private BigDecimal numbers;



     /**
      * 平仓手数
      */
     @TableField("close_number")
     private  BigDecimal closeNumber;

     /**
      * 平仓价
      */
     @TableField("exit_price")
     private java.math.BigDecimal exitPrice;

     /**
      * 平仓价值
      */
     @TableField("hand_price")
     private BigDecimal handPrice;

     /**
      * 止损价
      */
     @TableField("stop_loss")
     private java.math.BigDecimal stopLoss;

     /**
      * 止盈价
      */
     @TableField("stop_profit")
     private java.math.BigDecimal stopProfit;

     /**
      * 盈亏
      */
     @TableField("pl")
     private java.math.BigDecimal pl;
     /**
      * 配资盈亏
      */
     @TableField("give_pl")
     private java.math.BigDecimal givePl;


     /**
      * 总盈亏
      */
     @TableField("tpl")
     private java.math.BigDecimal tpl;



     /**
      * 平仓类型(手动平仓、止盈平仓、止损平仓、爆仓）
      */
     @TableField("exit_type")
     private String exitType;

     /**
      * 平仓时间
      */
     @TableField("exit_time")
     private java.util.Date exitTime;

     /**
      * 开仓价
      */
     @TableField("trade_price")
     private java.math.BigDecimal tradePrice;

     /**
      * 杠杆倍率名称
      */
     @TableField("lever_name")
     private String leverName;

     /**
      * 杠杆倍率
      */
     @TableField("lever_rate")
     private java.math.BigDecimal leverRate;

     /**
      * 开仓手续费
      */
     @TableField("fee")
     private java.math.BigDecimal fee;


     /**
      * 平仓手续费
      */
     @TableField("close_fee_price")
     private BigDecimal closeFeePrice;

     /**
      * 总额
      */
     @TableField("total_price")
     private java.math.BigDecimal totalPrice;

     /**
      * 仓位保证金
      */
     @TableField("position_price")
     private java.math.BigDecimal positionPrice;

     /**
      * 平仓保证金
      */
     @TableField("exit_position_price")
     private java.math.BigDecimal exitPositionPrice;

     /**
      * 配资
      */
     @TableField("give_price")
     private java.math.BigDecimal givePrice;

     /**
      * 版本号
      */
     @TableField("version")
     private Long version;

     /**
      * 平仓状态（未平仓、已平仓、撤单）
      */
     @TableField("status")
     private String status;

     /**
          下单币种
      */
     @TableField("coin")
     private String coin;

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
      * 订单是否过期
      */
     @TableField("enabled")
     private String enabled;

     /**
      * 是否穿仓
      */
     @TableField("crossed")
     private String crossed;

     /**
      * 穿仓金额
      */
     @TableField("csnumber")
     private BigDecimal csnumber;


     @TableField("open_fee_ratio")
     private BigDecimal openFeeRaito;

 }
