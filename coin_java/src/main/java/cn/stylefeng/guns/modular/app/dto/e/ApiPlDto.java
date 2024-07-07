package cn.stylefeng.guns.modular.app.dto.e;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 止盈止损设置
 */
@Data
public class ApiPlDto
{
    @NotNull
    private Long compactId;

//    @DecimalMin("0")
    private String stopLoss;

//    @DecimalMin("0")
    private String stopProfit;


}
