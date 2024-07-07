package cn.stylefeng.guns.modular.app.controller.market.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 行情bo
 */
@Data
@Accessors(chain = true)
public class TicketBo {
    //名称
    private String name;
    //最新价
    private String close;
    //涨跌幅
    private BigDecimal rate;
    //符号
    private String symbol;
    //折合人民币
    private BigDecimal cny;

}
