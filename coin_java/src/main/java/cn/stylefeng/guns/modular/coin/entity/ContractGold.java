package cn.stylefeng.guns.modular.coin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 黄金交易每一期
 */
@TableName("contract_gold")
@Data
public class ContractGold implements Serializable{
	@TableField("id")
	private Long id;
	/** 合约序号（如第1期，第2期，第3期 中的 1，2，3） */
	@TableField("gold_no")
	private Integer goldNo;
	/** 交易币种名称，格式：BTC/USDT */
	@TableField("symbol")
	private String symbol;
	/** 开盘价格 */
	@TableField("open_price")
	private BigDecimal openPrice;
	/** 开盘时间 */
	@TableField("open_time")
	private Long openTime;
	/** 收盘价格 */
	@TableField("close_price")
	private BigDecimal closePrice;
	/** 收盘时间 */
	@TableField("close_time")
	private Long closeTime;
	/** 买涨奖池总金额 */
	@TableField("total_buy")
	private BigDecimal totalBuy;
	/** 初始化买金额 */
	@TableField("init_buy")
	private BigDecimal initBuy;
	/** 买涨人数 */
	@TableField("total_Buy_Count")
	private Integer totalBuyCount;
	/** 买跌奖池总金额 */
	@TableField("total_sell")
	private BigDecimal totalSell;
	/** 初始化卖金额 */
	@TableField("init_sell")
	private BigDecimal initSell;
	/** 买跌人数 */
	@TableField("total_sell_Count")
	private Integer totalSellCount;
	/** 0投注中 1开奖中  2开奖结束 3撤销 */
	@TableField("status")
	private Integer status;
	/** 0待开奖 1涨  2跌  3平 4撤销 */
	@TableField("result")
	private Integer result;
	/** 平台盈利平台盈利（如平局、手续费等） */
	@TableField("total_Pl")
	private BigDecimal totalPl;
	/** 预设收盘价格 */
	@TableField("preset_Price")
	private BigDecimal presetPrice;
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