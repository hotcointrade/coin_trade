package cn.stylefeng.guns.modular.app.dto.otc;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 挂单出售dto
 */
@Data
@Accessors(chain = true)
public class SellDto extends BuyDto
{
    @NotBlank
    private String paymentId; //收款方式id
}
