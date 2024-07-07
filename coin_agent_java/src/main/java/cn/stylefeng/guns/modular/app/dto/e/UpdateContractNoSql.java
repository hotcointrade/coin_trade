package cn.stylefeng.guns.modular.app.dto.e;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 待更新队列 -合约
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class UpdateContractNoSql
{

    /**
     * 持仓保证金
     */
    private BigDecimal positionPrice;




    /**
     * 配资资产
     */
    private BigDecimal entrustPrice;
    /**
     * 持仓配资
     */
    private BigDecimal givePrice;

    /**
     * 1-买入  2-手动平仓
     */
    private int type=-1;

    /**
     * 持仓保证金 原持仓保证金量
     */
    private BigDecimal oldPosition;
}
