package cn.stylefeng.guns.modular.app.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 钱包账户dto
 */
@Data
@Accessors(chain = true)
public class ApiWalletDto
{
    //id
    private Long id;
    //类型
    private String type;
    //总额
    private String totalPrice;
    //可用
    private String usedPrice;

}
