package cn.stylefeng.guns.modular.coin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 黄金交易对
 */
@TableName("contract_gold_coin")
@Data
public class ContractGoldCoin implements Serializable{
	/** 黄金交易对 */
	@TableField("id")
	private Long id;
	/** 交易对 */
	@TableField("symbol")
	private String symbol;
	/** 交易名称 LTC/USDT预测合约 */
	@TableField("name")
	private String name;
	/** 币种 LTC */
	@TableField("coin_Symbol")
	private String coinSymbol;
	/** 基础量价币种 USDT */
	@TableField("base_Symbol")
	private String baseSymbol;
	/** 最新期号 */
	@TableField("max_gold_No")
	private Integer maxGoldNo;
	/** 排序 */
	@TableField("sort")
	private Integer sort;
	/** 交易币小数精度 */
	@TableField("coin_Scale")
	private Integer coinScale;
	/** 基币小数精度 */
	@TableField("base_coin_Scale")
	private Integer baseCoinScale;
	/** 启用/禁止 0/1 */
	@TableField("enable")
	private Integer enable;
	/** 显示 是/否 1/0 */
	@TableField("visible")
	private Integer visible;
	/** 平局处理 退回资金/平台通吃 1/0 */
	@TableField("tied_Type")
	private Integer tiedType;
	/** 允许看涨 允许/不允许 1/0 */
	@TableField("enable_Buy")
	private Integer enableBuy;
	/** 允许看跌 允许/不允许 1/0 */
	@TableField("enable_sell")
	private Integer enableSell;
	/** 允许投注金额 10,20,50,100,200,500,1000 */
	@TableField("amount")
	private String amount;
	/** 赔率 */
	@TableField("oods")
	private BigDecimal oods;
	/** 开仓手续费(默认0) */
	@TableField("fee_Percent")
	private BigDecimal feePercent;
	/** 赢家抽水费 默认千分之一 */
	@TableField("win_Fee_Percent")
	private BigDecimal winFeePercent;
	/** 如设置为0.005，则涨跌幅度小于0.5%的会被视为平局 */
	@TableField("ngnore_Percent")
	private BigDecimal ngnorePercent;
	/** 初始买涨奖池金额 */
	@TableField("init_Buy_Reward")
	private BigDecimal initBuyReward;
	/** 初始买跌奖池金额 */
	@TableField("init_Sell_Reward")
	private BigDecimal initSellReward;
	/** 黄金合约总盈利 */
	@TableField("total_Profit")
	private BigDecimal totalProfit;
	/** 投注时间周期(秒) 开始到开盘时间间隔（单位：秒， 默认5分钟） */
	@TableField("open_Time_Gap")
	private Integer openTimeGap;
	/** 开奖时间周期(秒) 开盘到收盘时间间隔（单位：秒， 默认5分钟 */
	@TableField("close_Time_Gap")
	private Integer closeTimeGap;
	/** 创建时间 */
	@TableField("CREATE_TIME")
	private Date createTime;
	/** 创建人 */
	@TableField("CREATE_USER")
	private Long createUser;
	/** 更新时间 */
	@TableField("UPDATE_TIME")
	private Date updateTime;
	/** 更新人 */
	@TableField("UPDATE_USER")
	private Long updateUser;
    
	
}