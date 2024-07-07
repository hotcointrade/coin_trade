package cn.stylefeng.guns.modular.app.dto.e;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
public class MatchDto
{


    private Long matchId;

    /**
     * 订单类型（买、卖）
     */
    private String matchType;

    /**
     * 成交量 btc
     */
    private String numbersB;

    /**
     * 委托量 btc
     */
    private String willNumberB;
    /**
     * 成交总额 USDT
     */
    private String numbersU;

    /**
     * 手续费
     */
    private String numberFee;


    /**
     * 委托单价
     */
    private String unit;

    /**
     * 成交均价
     */
    private String avgUnit;


    /**
     * 交易对
     */
    private String symbols;

    /**
     * 状态（未支付，已支付，部分撮合，全部撮合，已经撤销，完成结算，撤销结算）
     */
    private String status;
    /**
     * 时间
     */
    private Date createTime;

    private String orderNo;

}
