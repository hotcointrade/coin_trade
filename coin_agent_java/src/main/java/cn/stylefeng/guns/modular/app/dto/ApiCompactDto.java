package cn.stylefeng.guns.modular.app.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 合约订单DTO
 *
 */
@Data
public class ApiCompactDto {

    private Long compactId;
    /**
     * 订单号
     */
    private String orderNo;


    /**
     * 交易对
     */
    private String symbols;

    /**
     * 交易类型(BUY买入开多、SELL卖出开多)
     */
    private String compactType;


    /**
     * 杠杆倍率名称
     */
    private String leverName;


    /**
     * 持仓保证金
     */
    private String positionPrice;

    /**
     * 建仓价格
     */
    private String tradePrice;

    /**
     * 持仓手数
     */
    private BigDecimal numbers;

    /**
     * 每手的价值
     */
    private String handNumber;

    /**
     * 平仓手续费率
     */
    private  String exitFeeRatio;

    /**
     * 持仓价值
     */
    private String openHandPrice;

    /**
     * 开仓手续费
     */
    private String fee;

    /**
     * 止损价
     */
    private String stopLoss;
    /**
     * 止盈价
     */
    private String stopProfit;



    /**
     * 平仓保证金
     */
    private String exitPositionPrice;

    /**
     * 平仓价
     */
    private String exitPrice;


    /**
     * 平仓手数
     */
    private BigDecimal closeNumber;

    /**
     * 平仓价值
     */
    private String handPrice;

    /**
     * 平仓手续费
     */
    private String closeFeePrice;

    /**
     * 平仓类型(手动平仓、止盈平仓、止损平仓、爆仓）
     */
    private String exitType;

    /**
     * 盈亏额
     */
    private String lossProfit;

    /**
     * 盈亏率
     */
    private String lossProfitRatio;

    /**
     * 持仓时间或委托时间
     */
    private Date createTime;

    /**
     * 平仓时间
     */
    private Date exitTime;

}
