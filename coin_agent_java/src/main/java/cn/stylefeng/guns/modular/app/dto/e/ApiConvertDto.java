package cn.stylefeng.guns.modular.app.dto.e;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Data
public class ApiConvertDto
{
    //币种
    @NotBlank
    private String type;
    //从
    @Pattern(regexp = "^(WALLET|CONTRACT|CURRENCY|LEGAL|OPTION|FUTURES|MINING)$",message = "钱包类型有误")
    @NotBlank
    private String from;
    //到
    @Pattern(regexp = "^(WALLET|CONTRACT|CURRENCY|LEGAL|OPTION|FUTURES|MINING)$",message = "钱包类型有误")
    @NotBlank
    private String to;
    //划转数量
    @NotNull
    private BigDecimal numbers;
}
