package cn.stylefeng.guns.modular.app.dto.otc;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 *      * @param type 0-我要购买 1-我要出售
 *      * @param coin USDT 、
 *      * @param payMethod  支付方式 ("ALI_PAY,WE_CHAT,BANK", "全部")  ("ALI_PAY", "支付宝"), ("WE_CHAT", "微信"), ("BANK", "银行卡")
 *      * @param price  金额
 *      * @param number  数量
 */
@Data
public class TradeDto
{
    @NotNull
    private int type;
//    @NotBlank
    private String coin;

    private String payMethod;

    private BigDecimal price;
    private BigDecimal number;
}
