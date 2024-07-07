package cn.stylefeng.guns.modular.e.entity;

import cn.stylefeng.guns.modular.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 期货币种订单表
 */
@TableName("e_futures_compact")
@Data
@Accessors(chain = true)
public class FuturesCompact extends BaseEntity implements Serializable{
	@TableId(value="futures_compact_id", type= IdType.AUTO)
	private Long futuresCompactId;
	/** 订单号 */
	@TableField("order_no")
	private String orderNo;
	/** 用户 */
	@TableField("member_id")
	private Long memberId;
	/** 交易方式 */
	@TableField("deal_way")
	private String dealWay;
	/** 交易类型(买入开多、卖出开多) */
	@TableField("compact_type")
	private String compactType;
	/** 交易对 */
	@TableField("symbols")
	private String symbols;
	/** 单价 */
	@TableField("unit")
	private BigDecimal unit;
	/** 数量 */
	@TableField("numbers")
	private BigDecimal numbers;
	/** 平仓价 */
	@TableField("exit_price")
	private BigDecimal exitPrice;
	/** 止损价 */
	@TableField("stop_loss")
	private BigDecimal stopLoss;
	/** 止盈价 */
	@TableField("stop_profit")
	private BigDecimal stopProfit;
	/** 保证金盈亏 */
	@TableField("pl")
	private BigDecimal pl;
	/** 配资盈亏 */
	@TableField("give_pl")
	private BigDecimal givePl;
	/** 盈亏总额 */
	@TableField("tpl")
	private BigDecimal tpl;
	/** 平仓类型(手动平仓、止盈平仓、止损平仓、爆仓） */
	@TableField("exit_type")
	private String exitType;
	/** 平仓时间 */
	@TableField("exit_time")
	private Date exitTime;
	/** 开仓价 */
	@TableField("trade_price")
	private BigDecimal tradePrice;
	/** 杠杆倍率名称 */
	@TableField("lever_name")
	private String leverName;
	/** 杠杆倍率 */
	@TableField("lever_rate")
	private BigDecimal leverRate;
	/** 手续费 */
	@TableField("fee")
	private BigDecimal fee;
	/** 总额 */
	@TableField("total_price")
	private BigDecimal totalPrice;
	/** 保证金 */
	@TableField("position_price")
	private BigDecimal positionPrice;
	/** 配资 */
	@TableField("give_price")
	private BigDecimal givePrice;
	/** 版本号 */
	@TableField("version")
	private Long version;
	/** 平仓状态（未平仓、已平仓、撤单、委托中） */
	@TableField("status")
	private String status;
	/** 下单币种 */
	@TableField("coin")
	private String coin;
	/** 删除标志 */
	@TableField("del")
	private String del;
	/** 备注 */
	@TableField("remark")
	private String remark;

	/** 订单是否过期 Y/N */
	@TableField("enabled")
	private String enabled;
	/** 是否穿仓 Y/N */
	@TableField("crossed")
	private String crossed;
	/** 穿仓金额 */
	@TableField("csnumber")
	private BigDecimal csnumber;
	/** 每手的价值 */
	@TableField("hand_number")
	private Integer handNumber;
	/** 平仓手数 */
	@TableField("close_number")
	private BigDecimal closeNumber;
	/** 平仓价值 */
	@TableField("hand_price")
	private BigDecimal handPrice;
	/** 平仓手续费 */
	@TableField("close_fee_price")
	private BigDecimal closeFeePrice;
	@TableField("exit_position_price")
	private BigDecimal exitPositionPrice;
	@TableField("open_fee_ratio")
	private BigDecimal openFeeRatio;


}
