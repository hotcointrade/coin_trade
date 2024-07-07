package cn.stylefeng.guns.modular.app.vo.e;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class ApiWalletVo
{
    @Pattern(regexp = "^(BTC|USDT)$",message = "估值类型有误")
    @NotBlank
    private String valuation;

    //隐藏为0的资产 Y-"隐藏" N-"不隐藏" -默认不隐藏
    @NotBlank
    private String hide;

    //钱包类型
    @Pattern(regexp = "^(WALLET|CONTRACT|CURRENCY|LEGAL|OPTION|FUTURES|MINING)$",message = "钱包类型有误")
    @NotBlank
    private String type;

}
