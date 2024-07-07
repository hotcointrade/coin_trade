package cn.stylefeng.guns.modular.app.dto.e;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

/**
 * 币币交易dto
 */
@Data
public class ApiCurrencyDto
{

    //单价
    @DecimalMin("0")
    @NotNull
    private BigDecimal unit;

    //数量
    @DecimalMin("0")
    @NotNull
    private BigDecimal number;

    //类型 买-BUY、卖-SELL
    @Pattern(regexp = "^(BUY|SELL)$",message = "类型有误")
    @NotBlank
    private String matchType;

    //交易方式 MARKET-市价 LIMIT-限价
    @Pattern(regexp = "^(MARKET|LIMIT)$",message = "交易方式有误")
    @NotBlank
    private String dealWay;

    //交易对
    @NotBlank
    private String symbols;

}
