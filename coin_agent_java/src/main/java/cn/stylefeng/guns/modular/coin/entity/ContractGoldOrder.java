package cn.stylefeng.guns.modular.coin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 黄金交易对订单
 */
@TableName("contract_gold_order")
@Data
public class ContractGoldOrder implements Serializable{
	/** 黄金交易对 */
	@TableField("id")
	private Long id;
	/** 交易对 */
	@TableField("gold_id")
	private Long goldId;
	/** 合约序号（如第1期，第2期，第3期 中的 1，2，3） */
	@TableField("gold_No")
	private Integer goldNo;
	/** 用户id */
	@TableField("member_Id")
	private Long memberId;
	/** 交易币种名称，格式：BTC/USDT */
	@TableField("symbol")
	private String symbol;
	/** 币种 LTC */
	@TableField("coin_Symbol")
	private String coinSymbol;
	/** 基础量价币种 USDT */
	@TableField("base_Symbol")
	private String baseSymbol;
	/** 方向0看涨 1看跌 */
	@TableField("direction")
	private Integer direction;
	/** 参与结果 0待开始  1胜利  2失败  3平局 */
	@TableField("result")
	private Integer result;
	/** 手续费（当局结束时收取，开盘不收取且平局不收取，该部分金额需要先冻结） */
	@TableField("fee")
	private BigDecimal fee;
	/** 抽水费  */
	@TableField("win_Fee")
	private BigDecimal winFee;
	/** 抽水费  */
	@TableField("bet_Amount")
	private BigDecimal betAmount;
	/**  获奖金额（根据投注金额占总投注金额的比例，瓜分对向奖池）  */
	@TableField("reward_Amount")
	private BigDecimal rewardAmount;
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
	@TableField("status")
	private Integer status;
    
	
}