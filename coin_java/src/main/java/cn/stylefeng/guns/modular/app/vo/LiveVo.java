package cn.stylefeng.guns.modular.app.vo;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class LiveVo
{
    @NotNull
    @DecimalMin("0")
    private BigDecimal cny;
    @NotNull
    @DecimalMin("0")
    private BigDecimal md;
    @NotBlank
    private String payPwd;
    @NotBlank
    private String link;
}
