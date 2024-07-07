package cn.stylefeng.guns.modular.app.dto.e;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Data
public class ApiTransferDto
{

    /**
     * 转账账号
     */
    @NotBlank
    private String account;
    /**
     * 数量
     */
    @NotBlank
    private BigDecimal price;

    @NotBlank
    private String payPwd;

    @NotBlank
    @Pattern(regexp = "^(USDT|CNY)$",message = "格式有误")
    private String type;
}
