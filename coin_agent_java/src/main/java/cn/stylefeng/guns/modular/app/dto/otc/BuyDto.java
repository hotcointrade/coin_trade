package cn.stylefeng.guns.modular.app.dto.otc;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 挂单购买dto
 */
@Data
@Accessors(chain = true)
public class BuyDto
{
    @NotBlank
    private String coin;//币种
    @DecimalMin("0")
    @NotNull
    private BigDecimal unitPrice;//单价
    @DecimalMin("0")
    @NotNull
    private BigDecimal number;//数量
    @DecimalMin("0")
    @NotNull
    private BigDecimal lowNumber;//最小数量
    @DecimalMin("0")
    @NotNull
    private BigDecimal lowPrice;//最小金额
    @NotBlank
    private String payMethod;//付款方式

}
