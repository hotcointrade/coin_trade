package cn.stylefeng.guns.modular.app.dto.e;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CompactDto
{
    /**
     * id
     */
    private Long compactId;


    /**
     * 交易方式
     */
    private String dealWay;

    /**
     * 交易类型(买入开多、卖出开空)
     */
    private String compactType;

    /**
     * 交易对
     */
    private String symbols;

    /**
     * 单价
     */
    private java.math.BigDecimal unit;

    /**
     * 数量
     */
    private java.math.BigDecimal numbers;

    /**
     * 平仓价
     */
    private java.math.BigDecimal exitPrice;

    /**
     * 止损价
     */
    private java.math.BigDecimal stopLoss;

    /**
     * 止盈价
     */
    private java.math.BigDecimal stopProfit;

    /**
     * 盈亏
     */
    private java.math.BigDecimal pl;

    /**
     * 平仓类型(手动平仓、止盈平仓、止损平仓、爆仓）
     */
    private String exitType;

    /**
     * 平仓时间
     */
    private java.util.Date exitTime;

    /**
     * 开仓价
     */
    private java.math.BigDecimal tradePrice;

    /**
     * 杠杆倍率名称
     */
    private String leverName;

    /**
     * 杠杆倍率
     */
    private java.math.BigDecimal leverRate;

    /**
     * 手续费
     */
    private java.math.BigDecimal fee;

    /**
     * 总额
     */
    private java.math.BigDecimal totalPrice;

    /**
     * 保证金
     */
    private java.math.BigDecimal positionPrice;

    /**
     * 配资
     */
    private java.math.BigDecimal givePrice;



    /**
     * 平仓状态（未平仓、已平仓、撤单）
     */
    private String status;

}
