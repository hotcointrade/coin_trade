package cn.stylefeng.guns.modular.app.dto.e;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 充币dto
 */
@Data
public class ApiRechargeDto
{

    @NotBlank
    private String type;

    private String address;

    @NotNull
    private BigDecimal price;

    //凭证
    @NotBlank
    private String remark;

    private String payPwd;

}
