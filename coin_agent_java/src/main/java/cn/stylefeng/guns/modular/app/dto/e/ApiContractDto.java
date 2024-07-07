package cn.stylefeng.guns.modular.app.dto.e;

import io.swagger.models.auth.In;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class ApiContractDto
{
    //交易对
    @NotBlank
    private String symbols;

    //交易价格（市价的时候以该币种的行情价格，限价的时候以输入的交易价格传递）
    @DecimalMin("0.000001")
    private BigDecimal unit;
    //交易数量
    @DecimalMin("0.1")
    private BigDecimal numbers;

    //类型 买入开多-BUY、卖出开空-SELL
    @Pattern(regexp = "^(BUY|SELL)$",message = "类型有误")
    @NotBlank
    private String compactType;

    //交易方式 MARKET-市价 LIMIT-限价
    @Pattern(regexp = "^(MARKET|LIMIT)$",message = "交易方式有误")
    @NotBlank
    private String dealWay;

    //杠杆倍率id
    @NotNull
    private Long leverageId;

    //下单币种
    @NotBlank
    private String coin;


}
